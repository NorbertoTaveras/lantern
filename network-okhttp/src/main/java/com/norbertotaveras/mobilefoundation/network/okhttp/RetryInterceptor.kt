package com.norbertotaveras.mobilefoundation.network.okhttp

import java.io.IOException
import okhttp3.Interceptor
import okhttp3.Response

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

                response.close()
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

    interface Sleeper {
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
