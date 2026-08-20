package com.norbertotaveras.mobilefoundation.featureflags

/**
 * Source used to resolve a feature flag value.
 */
enum class FeatureFlagValueSource {
    /**
     * The flag fell back to a default value.
     */
    Default,
    /**
     * The flag value came from the backing provider.
     */
    Provider
}
