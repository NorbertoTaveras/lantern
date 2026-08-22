package com.norbertotaveras.lantern.featureflags

/**
 * Describes a feature flag that can be evaluated by a [FeatureFlagProvider].
 */
data class FeatureFlag(
    /**
     * Stable key used to look up the flag value.
     */
    val key: FeatureFlagKey,
    /**
     * Value used when the provider does not return an override.
     */
    val defaultValue: FeatureFlagValue = FeatureFlagValue.BooleanValue(false),
    /**
     * Optional human-readable description for tooling or documentation.
     */
    val description: String? = null,
    /**
     * Optional lightweight metadata for callers and tooling.
     */
    val metadata: Map<String, String> = emptyMap()
)
