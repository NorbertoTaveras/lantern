package com.norbertotaveras.lantern.network.okhttp

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NetworkConnectivityStateTest {

    @Test
    fun isUsableRequiresAvailableAndValidatedNetwork() {
        assertTrue(
            NetworkConnectivityState(
                isAvailable = true,
                isValidated = true,
                transports = setOf(NetworkTransport.Wifi)
            ).isUsable
        )
    }

    @Test
    fun isUsableIsFalseWhenNetworkIsNotValidated() {
        assertFalse(
            NetworkConnectivityState(
                isAvailable = true,
                isValidated = false,
                transports = setOf(NetworkTransport.Wifi)
            ).isUsable
        )
    }
}
