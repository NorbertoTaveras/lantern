package com.norbertotaveras.mobilefoundation.securestorage.internal

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.securestorage.SecureStorageErrorCodes
import com.norbertotaveras.mobilefoundation.securestorage.SecureToken
import com.norbertotaveras.mobilefoundation.securestorage.SecureTokenType
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
