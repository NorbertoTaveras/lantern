package com.norbertotaveras.mobilefoundation.featureflags

data class FeatureFlagDefaults(
    val values: Map<FeatureFlagKey, FeatureFlagValue> = emptyMap()
) {
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    operator fun plus(other: FeatureFlagDefaults): FeatureFlagDefaults {
        return FeatureFlagDefaults(values + other.values)
    }

    companion object {
        val Empty = FeatureFlagDefaults()
    }
}
