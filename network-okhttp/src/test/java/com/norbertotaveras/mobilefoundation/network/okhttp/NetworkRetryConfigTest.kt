package com.norbertotaveras.mobilefoundation.network.okhttp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
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

    @Test
    fun delayForRetrySaturatesOverflowToMaxDelay() {
        val config = NetworkRetryConfig(
            initialDelayMillis = Long.MAX_VALUE / 2,
            maxDelayMillis = Long.MAX_VALUE,
            backoffMultiplier = Double.MAX_VALUE
        )

        assertEquals(Long.MAX_VALUE, config.delayForRetry(2))
    }

    @Test
    fun delayForRetryKeepsZeroInitialDelayAtZero() {
        val config = NetworkRetryConfig(
            initialDelayMillis = 0,
            maxDelayMillis = 250,
            backoffMultiplier = Double.MAX_VALUE
        )

        assertEquals(0, config.delayForRetry(100))
    }

    @Test
    fun initRejectsInvalidRetryStatusCodes() {
        assertThrows(IllegalArgumentException::class.java) {
            NetworkRetryConfig(retryStatusCodes = setOf(99, 503))
        }
    }
}
