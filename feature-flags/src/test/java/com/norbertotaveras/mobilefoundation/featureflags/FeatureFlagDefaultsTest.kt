package com.norbertotaveras.mobilefoundation.featureflags

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
}
