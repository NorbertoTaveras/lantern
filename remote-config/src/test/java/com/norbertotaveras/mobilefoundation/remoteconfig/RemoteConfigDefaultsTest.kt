package com.norbertotaveras.mobilefoundation.remoteconfig

import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteConfigDefaultsTest {

    @Test
    fun plusMergesDefaultsWithRightHandValuesWinning() {
        val key = RemoteConfigKey.unsafe("welcome_enabled")
        val base = RemoteConfigDefaults(
            values = mapOf(key to RemoteConfigValue.BooleanValue(false))
        )
        val override = RemoteConfigDefaults(
            values = mapOf(key to RemoteConfigValue.BooleanValue(true))
        )

        val merged = base + override

        assertEquals(RemoteConfigValue.BooleanValue(true), merged.values[key])
    }
}
