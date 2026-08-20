package com.norbertotaveras.mobilefoundation.featureflags

/**
 * Stable error codes returned by feature flag APIs.
 */
object FeatureFlagErrorCodes {
    /**
     * Fallback code for unexpected feature flag failures.
     */
    const val UNKNOWN = "feature_flag_unknown"
    /**
     * A feature flag key failed validation.
     */
    const val INVALID_KEY = "feature_flag_invalid_key"
    /**
     * A requested flag value was not found.
     */
    const val VALUE_NOT_FOUND = "feature_flag_value_not_found"
    /**
     * A flag value exists but cannot be read as the expected type.
     */
    const val TYPE_MISMATCH = "feature_flag_type_mismatch"
    /**
     * Evaluating a flag failed.
     */
    const val EVALUATION_FAILED = "feature_flag_evaluation_failed"
}
