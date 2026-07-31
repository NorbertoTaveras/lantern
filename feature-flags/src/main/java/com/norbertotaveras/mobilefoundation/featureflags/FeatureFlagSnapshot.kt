package com.norbertotaveras.mobilefoundation.featureflags

data class FeatureFlagSnapshot(
    val values: Map<FeatureFlagKey, FeatureFlagValue>
) {
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    companion object {
        val Empty = FeatureFlagSnapshot(values = emptyMap())
    }
}
