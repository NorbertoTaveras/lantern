package com.norbertotaveras.mobilefoundation.featureflags

/**
 * Point-in-time view of active feature flag values.
 */
data class FeatureFlagSnapshot(
    /**
     * Active values keyed by [FeatureFlagKey].
     */
    val values: Map<FeatureFlagKey, FeatureFlagValue>
) {
    /**
     * Returns the active value for [flag], or [FeatureFlag.defaultValue] when absent.
     */
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    companion object {
        /**
         * Empty snapshot used before values are available.
         */
        val Empty = FeatureFlagSnapshot(values = emptyMap())
    }
}
