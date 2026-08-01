package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultNotificationPayloadParserTest {

    private val parser = DefaultNotificationPayloadParser()

    @Test
    fun parseMapsTitleBodyAndDeepLink() {
        val result = parser.parse(
            mapOf(
                "title" to "Sale",
                "body" to "Open the new deal",
                "deep_link" to "app://deals/42",
                "route" to "deals",
                "dl_param_id" to "42"
            )
        )

        assertTrue(result is SdkResult.Success)
        val payload = (result as SdkResult.Success).data
        assertEquals("Sale", payload.title)
        assertEquals("Open the new deal", payload.body)
        assertEquals("app://deals/42", payload.deepLink?.uri)
        assertEquals("deals", payload.deepLink?.route)
        assertEquals(mapOf("id" to "42"), payload.deepLink?.parameters)
    }

    @Test
    fun parseKeepsOriginalDataWhenNoKnownKeysExist() {
        val data = mapOf("custom" to "value")

        val result = parser.parse(data)

        assertTrue(result is SdkResult.Success)
        val payload = (result as SdkResult.Success).data
        assertEquals(null, payload.title)
        assertEquals(null, payload.body)
        assertEquals(null, payload.deepLink)
        assertEquals(data, payload.data)
    }
}
