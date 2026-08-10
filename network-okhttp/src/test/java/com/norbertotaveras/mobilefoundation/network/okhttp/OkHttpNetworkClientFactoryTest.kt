package com.norbertotaveras.mobilefoundation.network.okhttp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OkHttpNetworkClientFactoryTest {

    @Test
    fun createAddsOptionalSdkInterceptorsBeforeCallerInterceptors() {
        val client = OkHttpNetworkClientFactory().create(
            tokenProvider = TokenProvider { "token" },
            retryConfig = NetworkRetryConfig(),
            interceptors = listOf(NoOpInterceptor)
        )

        assertTrue(client.interceptors[0] is AuthHeaderInterceptor)
        assertTrue(client.interceptors[1] is RetryInterceptor)
        assertEquals(NoOpInterceptor, client.interceptors[2])
    }

    @Test
    fun createWithLoggingAddsLoggingBeforeCallerInterceptors() {
        val client = OkHttpNetworkClientFactory().createWithLogging(
            logger = NoOpLogger,
            tokenProvider = TokenProvider { "token" },
            retryConfig = NetworkRetryConfig(),
            interceptors = listOf(NoOpInterceptor)
        )

        assertTrue(client.interceptors[0] is AuthHeaderInterceptor)
        assertTrue(client.interceptors[1] is RetryInterceptor)
        assertTrue(client.interceptors[2] is NetworkLoggingInterceptor)
        assertEquals(NoOpInterceptor, client.interceptors[3])
    }

    private fun interface TokenProvider : com.norbertotaveras.mobilefoundation.network.okhttp.TokenProvider

    private object NoOpLogger : com.norbertotaveras.mobilefoundation.logging.SdkLogger {
        override fun debug(message: String) = Unit

        override fun info(message: String) = Unit

        override fun warning(message: String, throwable: Throwable?) = Unit

        override fun error(message: String, throwable: Throwable?) = Unit
    }

    private object NoOpInterceptor : okhttp3.Interceptor {
        override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
}
