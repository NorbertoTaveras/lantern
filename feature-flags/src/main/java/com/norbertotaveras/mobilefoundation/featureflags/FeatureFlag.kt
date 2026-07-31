package com.norbertotaveras.mobilefoundation.featureflags

data class FeatureFlag(
    val key: FeatureFlagKey,
    val defaultValue: FeatureFlagValue = FeatureFlagValue.BooleanValue(false),
    val description: String? = null,
    val metadata: Map<String, String> = emptyMap()
)
