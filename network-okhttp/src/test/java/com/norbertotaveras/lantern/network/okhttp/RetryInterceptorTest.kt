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
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class RetryInterceptorTest {

    private val interceptor = RetryInterceptor(
        config = NetworkRetryConfig(),
        sleeper = object : RetryInterceptor.Sleeper {
            override fun sleep(delayMillis: Long) = Unit
        }
    )

    @Test
    fun shouldRetryAllowsIdempotentMethodsForRetryableStatusCodes() {
        assertTrue(interceptor.shouldRetry(method = "GET", statusCode = 503))
        assertTrue(interceptor.shouldRetry(method = "PUT", statusCode = 429))
    }

    @Test
    fun shouldRetrySkipsNonIdempotentMethods() {
        assertFalse(interceptor.shouldRetry(method = "POST", statusCode = 503))
        assertFalse(interceptor.shouldRetry(method = "PATCH", exception = IOException("broken")))
    }

    @Test
    fun shouldRetrySkipsNonRetryableStatusCodes() {
        assertFalse(interceptor.shouldRetry(method = "GET", statusCode = 400))
    }

    @Test
    fun interceptUsesRetryAfterDelaySecondsForRetryableResponses() {
        val sleeper = RecordingSleeper()
        val client = retryingClient(sleeper = sleeper)

        val response = client.newCall(baseRequest).execute()

        assertEquals(200, response.code)
        assertEquals(listOf(3_000L), sleeper.delays)
        response.close()
    }

    @Test
    fun interceptCapsRetryAfterDelayToConfiguredMaxDelay() {
        val sleeper = RecordingSleeper()
        val client = retryingClient(
            sleeper = sleeper,
            maxDelayMillis = 750
        )

        val response = client.newCall(baseRequest).execute()

        assertEquals(200, response.code)
        assertEquals(listOf(750L), sleeper.delays)
        response.close()
    }

    @Test
    fun interceptUsesRetryAfterHttpDateForRetryableResponses() {
        val sleeper = RecordingSleeper()
        val client = retryingClient(
            sleeper = sleeper,
            maxDelayMillis = 750,
            retryAfter = "Fri, 31 Dec 9999 23:59:59 GMT"
        )

        val response = client.newCall(baseRequest).execute()

        assertEquals(200, response.code)
        assertEquals(listOf(750L), sleeper.delays)
        response.close()
    }

    @Test
    fun interceptFallsBackToBackoffForInvalidRetryAfterHeader() {
        val sleeper = RecordingSleeper()
        val client = retryingClient(
            sleeper = sleeper,
            retryAfter = "not-a-date"
        )

        val response = client.newCall(baseRequest).execute()

        assertEquals(200, response.code)
        assertEquals(listOf(50L), sleeper.delays)
        response.close()
    }

    @Test
    fun interceptRetriesIdempotentRequestsAfterIoFailure() {
        val sleeper = RecordingSleeper()
        val terminal = FailingThenSuccessInterceptor()
        val client = retryingClient(
            sleeper = sleeper,
            terminal = terminal
        )

        val response = client.newCall(baseRequest).execute()

        assertEquals(200, response.code)
        assertEquals(2, terminal.attempts)
        assertEquals(listOf(50L), sleeper.delays)
        response.close()
    }

    @Test
    fun interceptDoesNotRetryNonIdempotentRequestsAfterIoFailure() {
        val sleeper = RecordingSleeper()
        val terminal = FailingThenSuccessInterceptor()
        val client = retryingClient(
            sleeper = sleeper,
            terminal = terminal
        )
        val request = baseRequest.newBuilder()
            .post(ByteArray(0).toRequestBody())
            .build()

        assertThrows(IOException::class.java) {
            client.newCall(request).execute()
        }
        assertEquals(1, terminal.attempts)
        assertEquals(emptyList<Long>(), sleeper.delays)
    }

    private fun retryingClient(
        sleeper: RetryInterceptor.Sleeper,
        maxDelayMillis: Long = 5_000,
        retryAfter: String = "3",
        terminal: Interceptor = QueuedResponseInterceptor(
            response(statusCode = 503, retryAfter = retryAfter),
            response(statusCode = 200)
        )
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(
                RetryInterceptor(
                    config = NetworkRetryConfig(
                        maxRetries = 1,
                        initialDelayMillis = 50,
                        maxDelayMillis = maxDelayMillis,
                        retryStatusCodes = setOf(503)
                    ),
                    sleeper = sleeper
                )
            )
            .addInterceptor(terminal)
            .build()
    }

    private class RecordingSleeper : RetryInterceptor.Sleeper {
        val delays = mutableListOf<Long>()

        override fun sleep(delayMillis: Long) {
            delays += delayMillis
        }
    }

    private class QueuedResponseInterceptor(
        vararg responses: Response
    ) : Interceptor {
        private val responses = ArrayDeque(responses.toList())

        override fun intercept(chain: Interceptor.Chain): Response {
            return responses.removeFirst()
                .newBuilder()
                .request(chain.request())
                .build()
        }
    }

    private class FailingThenSuccessInterceptor : Interceptor {
        var attempts = 0

        override fun intercept(chain: Interceptor.Chain): Response {
            attempts += 1
            if (attempts == 1) {
                throw IOException("temporary")
            }

            return response(statusCode = 200)
                .newBuilder()
                .request(chain.request())
                .build()
        }
    }

    private companion object {
        val baseRequest: Request = Request.Builder()
            .url("https://example.com")
            .build()

        fun response(
            statusCode: Int,
            retryAfter: String? = null
        ): Response {
            return Response.Builder()
                .request(baseRequest)
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .message("OK")
                .apply {
                    if (retryAfter != null) {
                        header("Retry-After", retryAfter)
                    }
                }
                .body("".toResponseBody())
                .build()
        }
    }
}
