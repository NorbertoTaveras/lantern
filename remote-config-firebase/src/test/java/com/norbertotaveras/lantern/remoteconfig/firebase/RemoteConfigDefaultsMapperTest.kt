/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
