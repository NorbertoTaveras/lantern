package com.norbertotaveras.lantern.securestorage

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SecureStorageKeyTest {

    @Test
    fun `from returns normalized key when value is valid`() {
        val result = SecureStorageKey.from(" session:user_id ")

        assertTrue(result is SdkResult.Success)
        assertEquals("session:user_id", (result as SdkResult.Success).data.value)
    }

    @Test
    fun `from returns invalid key error when value is blank`() {
        val result = SecureStorageKey.from(" ")

        assertTrue(result is SdkResult.Failure)
        assertEquals(
            SecureStorageErrorCodes.INVALID_KEY,
            (result as SdkResult.Failure).error.code
        )
    }

    @Test
    fun `from returns invalid key error when value contains unsupported characters`() {
        val result = SecureStorageKey.from("session token")

        assertTrue(result is SdkResult.Failure)
        assertEquals(
            SecureStorageErrorCodes.INVALID_KEY,
            (result as SdkResult.Failure).error.code
        )
    }
}
