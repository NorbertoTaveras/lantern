package com.norbertotaveras.mobilefoundation.securestorage

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SecureTokenTest {

    @Test
    fun `isExpired is false when token has no expiration`() {
        val token = SecureToken(value = "token")

        assertFalse(token.isExpired(currentTimeMillis = 100L))
    }

    @Test
    fun `isExpired is false before expiration time`() {
        val token = SecureToken(
            value = "token",
            expiresAtEpochMillis = 200L
        )

        assertFalse(token.isExpired(currentTimeMillis = 199L))
    }

    @Test
    fun `isExpired is true at expiration time`() {
        val token = SecureToken(
            value = "token",
            expiresAtEpochMillis = 200L
        )

        assertTrue(token.isExpired(currentTimeMillis = 200L))
    }
}
