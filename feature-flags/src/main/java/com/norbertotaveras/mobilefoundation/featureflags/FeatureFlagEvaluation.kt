package com.norbertotaveras.mobilefoundation.featureflags

data class FeatureFlagEvaluation(
    val flag: FeatureFlag,
    val value: FeatureFlagValue,
    val source: FeatureFlagValueSource
) {
    fun isEnabled(): Boolean {
        return (value as? FeatureFlagValue.BooleanValue)?.value ?: false
    }
}
