package com.norbertotaveras.mobilefoundation.featureflags

/**
 * Result of evaluating a [FeatureFlag].
 */
data class FeatureFlagEvaluation(
    /**
     * Flag that was evaluated.
     */
    val flag: FeatureFlag,
    /**
     * Resolved value for [flag].
     */
    val value: FeatureFlagValue,
    /**
     * Source of the resolved value.
     */
    val source: FeatureFlagValueSource
) {
    /**
     * Convenience helper for boolean flags.
     *
     * Non-boolean values are treated as disabled.
     */
    fun isEnabled(): Boolean {
        return (value as? FeatureFlagValue.BooleanValue)?.value ?: false
    }
}
