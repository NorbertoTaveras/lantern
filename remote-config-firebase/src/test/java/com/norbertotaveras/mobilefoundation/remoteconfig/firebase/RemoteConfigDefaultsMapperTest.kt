package com.norbertotaveras.mobilefoundation.remoteconfig.firebase

import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigKey
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigValue
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
