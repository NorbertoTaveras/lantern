package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AnalyticsEventNameTest {

    @Test
    fun fromAcceptsValidEventName() {
        val result = AnalyticsEventName.from("checkout_started")

        assertTrue(result is SdkResult.Success)
        assertEquals("checkout_started", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidEventName() {
        val result = AnalyticsEventName.from("1 checkout")

        assertTrue(result is SdkResult.Failure)
        assertEquals(AnalyticsErrorCodes.INVALID_EVENT_NAME, (result as SdkResult.Failure).error.code)
    }
}
