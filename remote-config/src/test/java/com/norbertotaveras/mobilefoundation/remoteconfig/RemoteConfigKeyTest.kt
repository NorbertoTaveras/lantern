package com.norbertotaveras.mobilefoundation.remoteconfig

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RemoteConfigKeyTest {

    @Test
    fun fromReturnsNormalizedKeyForValidValue() {
        val result = RemoteConfigKey.from(" welcome_enabled ")

        assertTrue(result is SdkResult.Success)
        assertEquals("welcome_enabled", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidKey() {
        val result = RemoteConfigKey.from("1-welcome")

        assertTrue(result is SdkResult.Failure)
        assertEquals(RemoteConfigErrorCodes.INVALID_KEY, (result as SdkResult.Failure).error.code)
    }
}
