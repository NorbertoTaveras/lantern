package com.norbertotaveras.mobilefoundation.featureflags

sealed interface FeatureFlagValue {
    data class BooleanValue(val value: Boolean) : FeatureFlagValue
    data class DoubleValue(val value: Double) : FeatureFlagValue
    data class LongValue(val value: Long) : FeatureFlagValue
    data class StringValue(val value: String) : FeatureFlagValue
}
