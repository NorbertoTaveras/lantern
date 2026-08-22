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
