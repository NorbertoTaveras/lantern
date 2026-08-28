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

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
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
    fun interceptSkipsHeaderWhenTokenContainsControlCharacters() {
        val recorder = RecordingTerminalInterceptor()
        val client = clientWith(
            AuthHeaderInterceptor(tokenProvider = TokenProvider { "abc\n123" }),
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

    @Test
    fun initAllowsValidAuthSchemeWithWhitespace() {
        val recorder = RecordingTerminalInterceptor()
        val client = clientWith(
            AuthHeaderInterceptor(
                tokenProvider = TokenProvider { "abc123" },
                scheme = " Bearer "
            ),
            recorder
        )

        client.newCall(baseRequest).execute().close()

        assertEquals("Bearer abc123", recorder.request?.header("Authorization"))
    }

    @Test
    fun initRejectsMalformedHeaderName() {
        assertThrows(IllegalArgumentException::class.java) {
            AuthHeaderInterceptor(
                tokenProvider = TokenProvider { "abc123" },
                headerName = "X Auth Token"
            )
        }
    }

    @Test
    fun initRejectsMalformedAuthScheme() {
        assertThrows(IllegalArgumentException::class.java) {
            AuthHeaderInterceptor(
                tokenProvider = TokenProvider { "abc123" },
                scheme = "Bearer Token"
            )
        }
    }

    private fun interface TokenProvider : com.norbertotaveras.lantern.network.okhttp.TokenProvider

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
