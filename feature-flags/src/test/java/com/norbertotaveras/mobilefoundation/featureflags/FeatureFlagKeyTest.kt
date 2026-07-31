package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FeatureFlagKeyTest {

    @Test
    fun fromReturnsNormalizedKeyForValidValue() {
        val result = FeatureFlagKey.from(" checkout.new-flow ")

        assertTrue(result is SdkResult.Success)
        assertEquals("checkout.new-flow", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidKey() {
        val result = FeatureFlagKey.from("1 checkout")

        assertTrue(result is SdkResult.Failure)
        assertEquals(FeatureFlagErrorCodes.INVALID_KEY, (result as SdkResult.Failure).error.code)
    }
}
