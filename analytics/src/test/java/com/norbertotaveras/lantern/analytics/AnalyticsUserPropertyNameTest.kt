package com.norbertotaveras.lantern.analytics

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsUserPropertyNameTest {

    @Test
    fun fromAcceptsValidPropertyName() {
        val result = AnalyticsUserPropertyName.from("subscription_tier")

        assertTrue(result is SdkResult.Success)
        assertEquals("subscription_tier", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsBlankPropertyName() {
        val result = AnalyticsUserPropertyName.from(" ")

        assertTrue(result is SdkResult.Failure)
        assertEquals(AnalyticsErrorCodes.INVALID_PROPERTY_NAME, (result as SdkResult.Failure).error.code)
    }
}
