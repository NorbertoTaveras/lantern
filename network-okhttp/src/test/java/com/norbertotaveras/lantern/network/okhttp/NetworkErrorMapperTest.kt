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

import java.net.SocketTimeoutException
import java.net.UnknownHostException
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkErrorMapperTest {

    private val mapper = NetworkErrorMapper()

    @Test
    fun mapReturnsTimeoutForSocketTimeoutException() {
        val error = mapper.map(SocketTimeoutException("timed out"))

        assertEquals(NetworkErrorCodes.TIMEOUT, error.code)
        assertEquals("timed out", error.message)
    }

    @Test
    fun mapReturnsNoConnectionForUnknownHostException() {
        val error = mapper.map(UnknownHostException("offline"))

        assertEquals(NetworkErrorCodes.NO_CONNECTION, error.code)
        assertEquals("offline", error.message)
    }

    @Test
    fun mapUsesDefaultMessageWhenThrowableMessageIsBlank() {
        val error = mapper.map(RuntimeException(""))

        assertEquals(NetworkErrorCodes.UNKNOWN, error.code)
        assertEquals("Network request failed.", error.message)
    }

    @Test
    fun retryExhaustedIncludesAttemptMetadata() {
        val error = mapper.retryExhausted(attempts = 3)

        assertEquals(NetworkErrorCodes.RETRY_EXHAUSTED, error.code)
        assertEquals("3", error.metadata["attempts"])
    }
}
