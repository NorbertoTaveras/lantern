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

package com.norbertotaveras.lantern.securestorage.internal

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.SecureStorageErrorCodes
import com.norbertotaveras.lantern.securestorage.SecureToken
import com.norbertotaveras.lantern.securestorage.SecureTokenType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SecureTokenCodecTest {

    private val codec = SecureTokenCodec()

    @Test
    fun `encode and decode round trip token values`() {
        val token = SecureToken(
            value = "id-token",
            type = SecureTokenType.IdToken,
            expiresAtEpochMillis = 123L,
            metadata = mapOf("issuer" to "firebase")
        )

        val encoded = codec.encode(token)
        assertTrue(encoded is SdkResult.Success)

        val decoded = codec.decode((encoded as SdkResult.Success).data)

        assertTrue(decoded is SdkResult.Success)
        assertEquals(token, (decoded as SdkResult.Success).data)
    }

    @Test
    fun `decode returns read failure for invalid json`() {
        val decoded = codec.decode("not-json")

        assertTrue(decoded is SdkResult.Failure)
        assertEquals(
            SecureStorageErrorCodes.READ_FAILED,
            (decoded as SdkResult.Failure).error.code
        )
    }

    @Test
    fun `decode returns read failure for unknown token type`() {
        val decoded = codec.decode(
            """
            {
              "value": "token",
              "type": "Unknown",
              "metadata": {}
            }
            """.trimIndent()
        )

        assertTrue(decoded is SdkResult.Failure)
        assertEquals(
            SecureStorageErrorCodes.READ_FAILED,
            (decoded as SdkResult.Failure).error.code
        )
    }

    @Test
    fun `decode returns read failure for blank token value`() {
        val decoded = codec.decode(
            """
            {
              "value": " ",
              "type": "Bearer",
              "metadata": {}
            }
            """.trimIndent()
        )

        assertTrue(decoded is SdkResult.Failure)
        assertEquals(
            SecureStorageErrorCodes.READ_FAILED,
            (decoded as SdkResult.Failure).error.code
        )
    }
}
