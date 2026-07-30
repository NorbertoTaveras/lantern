package com.norbertotaveras.mobilefoundation.network.okhttp

import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkRetryConfigTest {

    @Test
    fun delayForRetryAppliesExponentialBackoffAndMaxDelay() {
        val config = NetworkRetryConfig(
            initialDelayMillis = 100,
            maxDelayMillis = 250,
            backoffMultiplier = 2.0
        )

        assertEquals(100, config.delayForRetry(1))
        assertEquals(200, config.delayForRetry(2))
        assertEquals(250, config.delayForRetry(3))
    }
}
