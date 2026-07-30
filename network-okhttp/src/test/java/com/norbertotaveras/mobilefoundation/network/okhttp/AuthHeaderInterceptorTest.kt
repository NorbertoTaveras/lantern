package com.norbertotaveras.mobilefoundation.network.okhttp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AuthHeaderInterceptorTest {

    @Test
    fun interceptAddsBearerTokenWhenTokenIsAvailable() {
        val recorder = RecordingTerminalInterceptor()
        val client = clientWith(
            AuthHeaderInterceptor(tokenProvider = TokenProvider { "abc123" }),
            recorder
        )

        client.newCall(baseRequest).execute().close()

        assertEquals("Bearer abc123", recorder.request?.header("Authorization"))
    }

    @Test
    fun interceptSkipsHeaderWhenTokenIsBlank() {
        val recorder = RecordingTerminalInterceptor()
        val client = clientWith(
            AuthHeaderInterceptor(tokenProvider = TokenProvider { " " }),
            recorder
        )

        client.newCall(baseRequest).execute().close()

        assertNull(recorder.request?.header("Authorization"))
    }

    @Test
    fun interceptPreservesExistingHeaderByDefault() {
        val request = baseRequest.newBuilder()
            .header("Authorization", "Basic existing")
            .build()
        val recorder = RecordingTerminalInterceptor()
        val client = clientWith(
            AuthHeaderInterceptor(tokenProvider = TokenProvider { "abc123" }),
            recorder
        )

        client.newCall(request).execute().close()

        assertEquals("Basic existing", recorder.request?.header("Authorization"))
    }

    @Test
    fun interceptReplacesExistingHeaderWhenRequested() {
        val request = baseRequest.newBuilder()
            .header("Authorization", "Basic existing")
            .build()
        val recorder = RecordingTerminalInterceptor()
        val client = clientWith(
            AuthHeaderInterceptor(
                tokenProvider = TokenProvider { "abc123" },
                replaceExisting = true
            ),
            recorder
        )

        client.newCall(request).execute().close()

        assertEquals("Bearer abc123", recorder.request?.header("Authorization"))
    }

    private fun interface TokenProvider : com.norbertotaveras.mobilefoundation.network.okhttp.TokenProvider

    private fun clientWith(vararg interceptors: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                interceptors.forEach(::addInterceptor)
            }
            .build()
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
