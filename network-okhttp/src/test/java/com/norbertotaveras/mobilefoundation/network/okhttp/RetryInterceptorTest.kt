package com.norbertotaveras.mobilefoundation.network.okhttp

import java.io.IOException
import org.junit.Assert.assertFalse
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
}
