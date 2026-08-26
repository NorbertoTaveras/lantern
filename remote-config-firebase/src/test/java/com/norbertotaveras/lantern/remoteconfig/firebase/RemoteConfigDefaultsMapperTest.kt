package com.norbertotaveras.lantern.remoteconfig.firebase

import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.firebase.internal.toFirebaseDefaults
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteConfigDefaultsMapperTest {

    @Test
    fun toFirebaseDefaultsMapsTypedValuesToPrimitiveValues() {
        val defaults = RemoteConfigDefaults(
            mapOf(
                RemoteConfigKey.unsafe("enabled") to RemoteConfigValue.BooleanValue(true),
                RemoteConfigKey.unsafe("ratio") to RemoteConfigValue.DoubleValue(1.5),
                RemoteConfigKey.unsafe("count") to RemoteConfigValue.LongValue(3),
                RemoteConfigKey.unsafe("title") to RemoteConfigValue.StringValue("Hello")
            )
        )

        val result = defaults.toFirebaseDefaults()

        assertEquals(true, result["enabled"])
        assertEquals(1.5, result["ratio"])
        assertEquals(3L, result["count"])
        assertEquals("Hello", result["title"])
    }
}
