package com.norbertotaveras.mobilefoundation.featureflags

/**
 * Typed feature flag value exposed by provider-neutral APIs.
 */
sealed interface FeatureFlagValue {
    /**
     * Boolean flag value.
     */
    data class BooleanValue(val value: Boolean) : FeatureFlagValue
    /**
     * Double flag value.
     */
    data class DoubleValue(val value: Double) : FeatureFlagValue
    /**
     * Long flag value.
     */
    data class LongValue(val value: Long) : FeatureFlagValue
    /**
     * String flag value.
     */
    data class StringValue(val value: String) : FeatureFlagValue
}
