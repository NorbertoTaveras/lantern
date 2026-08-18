package com.norbertotaveras.mobilefoundation.network.okhttp

import com.norbertotaveras.mobilefoundation.logging.SdkLogger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkLoggingInterceptorTest {

    @Test
    fun interceptLogsBasicRequestAndResponse() {
        val logger = RecordingLogger()
        val client = clientWith(NetworkLoggingInterceptor(logger))

        client.newCall(baseRequest).execute().close()

        assertTrue(logger.infoMessages.any { it.contains("--> GET https://example.com/") })
        assertTrue(logger.infoMessages.any { it.contains("<-- 200 OK https://example.com/") })
    }

    @Test
    fun interceptRedactsSensitiveHeaders() {
        val logger = RecordingLogger()
        val request = baseRequest.newBuilder()
            .header("Authorization", "Bearer secret")
            .header("X-Trace-Id", "trace-123")
            .build()
        val client = clientWith(NetworkLoggingInterceptor(logger, NetworkLoggingLevel.Headers))

        client.newCall(request).execute().close()

        assertTrue(logger.debugMessages.any { it == "Authorization: ${NetworkLoggingInterceptor.REDACTED_VALUE}" })
        assertTrue(logger.debugMessages.any { it == "X-Trace-Id: trace-123" })
        assertFalse(logger.debugMessages.any { it.contains("Bearer secret") })
    }

    @Test
    fun interceptRedactsCustomHeadersCaseInsensitively() {
        val logger = RecordingLogger()
        val request = baseRequest.newBuilder()
            .header("X-Private-Token", "secret")
            .build()
        val client = clientWith(
            NetworkLoggingInterceptor(
                logger = logger,
                level = NetworkLoggingLevel.Headers,
                redactedHeaders = setOf("x-private-token")
            )
        )

        client.newCall(request).execute().close()

        assertTrue(logger.debugMessages.any { it == "X-Private-Token: ${NetworkLoggingInterceptor.REDACTED_VALUE}" })
        assertFalse(logger.debugMessages.any { it.contains("secret") })
    }

    @Test
    fun initRejectsMalformedRedactedHeaderNames() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkLoggingInterceptor(
                logger = RecordingLogger(),
                redactedHeaders = setOf("X Private Token")
            )
        }
    }

    private fun clientWith(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .addInterceptor(TerminalInterceptor)
            .build()
    }

    private object TerminalInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            return Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .build()
        }
    }

    private class RecordingLogger : SdkLogger {
        val debugMessages = mutableListOf<String>()
        val infoMessages = mutableListOf<String>()

        override fun debug(message: String) {
            debugMessages += message
        }

        override fun info(message: String) {
            infoMessages += message
        }

        override fun warning(message: String, throwable: Throwable?) = Unit

        override fun error(message: String, throwable: Throwable?) = Unit
    }

    private companion object {
        val baseRequest: Request = Request.Builder()
            .url("https://example.com")
            .build()
    }
}
