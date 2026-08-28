/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.network.okhttp

import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Retries idempotent requests after transient HTTP statuses or IO failures.
 *
 * The interceptor honors valid `Retry-After` response headers and otherwise uses
 * [NetworkRetryConfig.delayForRetry]. Non-idempotent request methods are never retried.
 */
class RetryInterceptor(
    private val config: NetworkRetryConfig = NetworkRetryConfig(),
    private val sleeper: Sleeper = ThreadSleeper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var attempt = 0
        var lastFailure: IOException? = null

        while (attempt <= config.maxRetries) {
            try {
                val response = chain.proceed(request)
                if (!shouldRetry(request.method, response.code) || attempt == config.maxRetries) {
                    return response
                }

                val retryDelayMillis = retryAfterDelayMillis(response) ?: config.delayForRetry(attempt + 1)
                response.close()
                attempt += 1
                sleeper.sleep(retryDelayMillis)
                continue
            } catch (exception: IOException) {
                lastFailure = exception
                if (!shouldRetry(request.method, exception) || attempt == config.maxRetries) {
                    throw exception
                }
            }

            attempt += 1
            sleeper.sleep(config.delayForRetry(attempt))
        }

        throw lastFailure ?: IOException("Network retry attempts exhausted.")
    }

    internal fun shouldRetry(method: String, statusCode: Int): Boolean {
        return method.isRetryableHttpMethod() && statusCode in config.retryStatusCodes
    }

    internal fun shouldRetry(method: String, exception: IOException): Boolean {
        return method.isRetryableHttpMethod()
    }

    private fun retryAfterDelayMillis(response: Response): Long? {
        val retryAfter = response.header("Retry-After")?.trim() ?: return null
        val millisPerSecond = 1_000L
        val delaySeconds = retryAfter.toLongOrNull()?.takeIf { it >= 0 }

        if (delaySeconds != null) {
            val delayMillis = delaySeconds
                .coerceAtMost(Long.MAX_VALUE / millisPerSecond)
                .times(millisPerSecond)

            return delayMillis.coerceAtMost(config.maxDelayMillis)
        }

        val delayMillis = retryAfterHttpDateDelayMillis(retryAfter) ?: return null
        return delayMillis.coerceAtMost(config.maxDelayMillis)
    }

    private fun retryAfterHttpDateDelayMillis(retryAfter: String): Long? {
        val retryAtMillis = try {
            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US)
                .apply {
                    isLenient = false
                    timeZone = TimeZone.getTimeZone("GMT")
                }
                .parse(retryAfter)
                ?.time
        } catch (exception: ParseException) {
            null
        } ?: return null

        return (retryAtMillis - System.currentTimeMillis()).coerceAtLeast(0L)
    }

    /**
     * Sleeps between retry attempts.
     *
     * This is injectable so tests and custom hosts can avoid blocking threads directly.
     */
    interface Sleeper {
        /**
         * Waits for [delayMillis] before the next retry attempt.
         */
        fun sleep(delayMillis: Long)
    }

    private object ThreadSleeper : Sleeper {
        override fun sleep(delayMillis: Long) {
            if (delayMillis > 0) {
                Thread.sleep(delayMillis)
            }
        }
    }
}

private fun String.isRetryableHttpMethod(): Boolean {
    return uppercase() in setOf("GET", "HEAD", "OPTIONS", "TRACE", "PUT", "DELETE")
}
