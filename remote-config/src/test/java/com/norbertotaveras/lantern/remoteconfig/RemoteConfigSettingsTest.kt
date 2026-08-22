package com.norbertotaveras.lantern.remoteconfig

import org.junit.Assert.assertThrows
import org.junit.Test

class RemoteConfigSettingsTest {

    @Test
    fun initRejectsNegativeMinimumFetchInterval() {
        assertThrows(IllegalArgumentException::class.java) {
            RemoteConfigSettings(minimumFetchIntervalMillis = -1)
        }
    }

    @Test
    fun initRejectsNegativeFetchTimeout() {
        assertThrows(IllegalArgumentException::class.java) {
            RemoteConfigSettings(fetchTimeoutMillis = -1)
        }
    }
}
