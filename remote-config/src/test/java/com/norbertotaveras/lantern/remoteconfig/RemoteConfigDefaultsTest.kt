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

package com.norbertotaveras.lantern.remoteconfig

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
