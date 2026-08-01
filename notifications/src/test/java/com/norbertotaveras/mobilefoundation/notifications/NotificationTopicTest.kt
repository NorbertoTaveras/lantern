package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NotificationTopicTest {

    @Test
    fun fromReturnsNormalizedTopicForValidValue() {
        val result = NotificationTopic.from(" product.updates ")

        assertTrue(result is SdkResult.Success)
        assertEquals("product.updates", (result as SdkResult.Success).data.value)
    }

    @Test
    fun fromRejectsInvalidTopic() {
        val result = NotificationTopic.from("product updates")

        assertTrue(result is SdkResult.Failure)
        assertEquals(NotificationErrorCodes.INVALID_TOPIC, (result as SdkResult.Failure).error.code)
    }
}
