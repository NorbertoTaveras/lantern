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

package com.norbertotaveras.lantern.featureflags

import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import org.junit.Assert.assertEquals
import org.junit.Test

class FeatureFlagDefaultsTest {

    @Test
    fun valueForUsesConfiguredDefaultBeforeFlagDefault() {
        val key = FeatureFlagKey.unsafe("new_checkout")
        val flag = FeatureFlag(
            key = key,
            defaultValue = FeatureFlagValue.BooleanValue(false)
        )
        val defaults = FeatureFlagDefaults(
            values = mapOf(key to FeatureFlagValue.BooleanValue(true))
        )

        assertEquals(FeatureFlagValue.BooleanValue(true), defaults.valueFor(flag))
    }

    @Test
    fun plusMergesWithRightHandValuesWinning() {
        val key = FeatureFlagKey.unsafe("new_checkout")
        val base = FeatureFlagDefaults(mapOf(key to FeatureFlagValue.BooleanValue(false)))
        val override = FeatureFlagDefaults(mapOf(key to FeatureFlagValue.BooleanValue(true)))

        assertEquals(FeatureFlagValue.BooleanValue(true), (base + override).values[key])
    }

    @Test
    fun toRemoteConfigDefaultsMapsFeatureFlagValues() {
        val defaults = FeatureFlagDefaults(
            values = mapOf(
                FeatureFlagKey.unsafe("welcome_enabled") to FeatureFlagValue.BooleanValue(true),
                FeatureFlagKey.unsafe("paywall_variant") to FeatureFlagValue.StringValue("control"),
                FeatureFlagKey.unsafe("checkout_limit") to FeatureFlagValue.LongValue(5L),
                FeatureFlagKey.unsafe("discount_rate") to FeatureFlagValue.DoubleValue(0.25)
            )
        )

        val remoteDefaults = defaults.toRemoteConfigDefaults()

        assertEquals(
            RemoteConfigValue.BooleanValue(true),
            remoteDefaults.values[RemoteConfigKey.unsafe("welcome_enabled")]
        )
        assertEquals(
            RemoteConfigValue.StringValue("control"),
            remoteDefaults.values[RemoteConfigKey.unsafe("paywall_variant")]
        )
        assertEquals(
            RemoteConfigValue.LongValue(5L),
            remoteDefaults.values[RemoteConfigKey.unsafe("checkout_limit")]
        )
        assertEquals(
            RemoteConfigValue.DoubleValue(0.25),
            remoteDefaults.values[RemoteConfigKey.unsafe("discount_rate")]
        )
    }
}
