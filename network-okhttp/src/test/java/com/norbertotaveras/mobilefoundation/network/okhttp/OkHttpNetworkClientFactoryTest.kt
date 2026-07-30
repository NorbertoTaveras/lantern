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

    private fun interface TokenProvider : com.norbertotaveras.mobilefoundation.network.okhttp.TokenProvider

    private object NoOpInterceptor : okhttp3.Interceptor {
        override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
            return chain.proceed(chain.request())
        }
    }
}
