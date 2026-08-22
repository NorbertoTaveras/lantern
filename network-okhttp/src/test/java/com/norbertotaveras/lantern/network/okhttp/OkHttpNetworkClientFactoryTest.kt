package com.norbertotaveras.lantern.network.okhttp

import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
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
    fun createAddsConfiguredDefaultHeaders() {
        val recorder = RecordingTerminalInterceptor()
        val client = OkHttpNetworkClientFactory(
            config = NetworkConfig(defaultHeaders = mapOf("X-Sdk-Client" to "lantern"))
        ).create(interceptors = listOf(recorder))

        client.newCall(baseRequest).execute().close()

        assertEquals("lantern", recorder.request?.header("X-Sdk-Client"))
    }

    @Test
    fun createPreservesRequestHeadersOverConfiguredDefaultHeaders() {
        val recorder = RecordingTerminalInterceptor()
        val client = OkHttpNetworkClientFactory(
            config = NetworkConfig(defaultHeaders = mapOf("X-Sdk-Client" to "lantern"))
        ).create(interceptors = listOf(recorder))
        val request = baseRequest.newBuilder()
            .header("X-Sdk-Client", "request-specific")
            .build()

        client.newCall(request).execute().close()

        assertEquals("request-specific", recorder.request?.header("X-Sdk-Client"))
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

    @Test
    fun createWithLoggingSkipsLoggingInterceptorWhenDisabled() {
        val client = OkHttpNetworkClientFactory().createWithLogging(
            logger = NoOpLogger,
            loggingLevel = NetworkLoggingLevel.None,
            interceptors = listOf(NoOpInterceptor)
        )

        assertEquals(listOf(NoOpInterceptor), client.interceptors)
    }

    @Test
    fun createAddsCallerNetworkInterceptors() {
        val client = OkHttpNetworkClientFactory().create(
            networkInterceptors = listOf(NoOpInterceptor)
        )

        assertEquals(listOf(NoOpInterceptor), client.networkInterceptors)
    }

    private fun interface TokenProvider : com.norbertotaveras.lantern.network.okhttp.TokenProvider

    private object NoOpLogger : com.norbertotaveras.lantern.logging.SdkLogger {
        override fun debug(message: String) = Unit

        override fun info(message: String) = Unit

        override fun warning(message: String, throwable: Throwable?) = Unit

        override fun error(message: String, throwable: Throwable?) = Unit
    }

    private object NoOpInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return chain.proceed(chain.request())
        }
    }

    private class RecordingTerminalInterceptor : Interceptor {
        var request: Request? = null

        override fun intercept(chain: Interceptor.Chain): Response {
            request = chain.request()
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }
    }

    private companion object {
        val baseRequest: Request = Request.Builder()
            .url("https://example.com")
            .build()
    }
}
