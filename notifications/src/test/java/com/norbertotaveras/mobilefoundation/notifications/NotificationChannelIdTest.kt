package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationChannelIdTest {

    @Test
    fun fromReturnsNormalizedChannelIdForValidValue() {
        val result = NotificationChannelId.from(" product_alerts ")

        assertTrue(result is SdkResult.Success)
        assertEquals("product_alerts", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidChannelId() {
        val result = NotificationChannelId.from("1_product_alerts")

        assertTrue(result is SdkResult.Failure)
        assertEquals(NotificationErrorCodes.INVALID_CHANNEL_ID, (result as SdkResult.Failure).error.code)
    }
}
