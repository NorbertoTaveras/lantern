package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test

class NoOpAnalyticsProviderTest {

    @Test
    fun trackReturnsSuccess() = runBlocking {
        val provider = NoOpAnalyticsProvider()
        val event = AnalyticsEvent(AnalyticsEventName.unsafe("app_started"))

        val result = provider.track(event)

        assertTrue(result is SdkResult.Success)
    }
}
