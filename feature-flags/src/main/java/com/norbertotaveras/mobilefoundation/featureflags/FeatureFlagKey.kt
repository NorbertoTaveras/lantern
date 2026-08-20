package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.featureflags.internal.FeatureFlagKeyValidator

/**
 * Validated feature flag key.
 *
 * Prefer [from] for dynamic values so invalid keys are returned as [SdkResult.Failure].
 * [unsafe] is intended for trusted constants.
 */
@JvmInline
value class FeatureFlagKey private constructor(val value: String) {
    companion object {
        /**
         * Creates a [FeatureFlagKey] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<FeatureFlagKey> {
            return when (val result = FeatureFlagKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(FeatureFlagKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [FeatureFlagKey] without validation.
         */
        fun unsafe(value: String): FeatureFlagKey {
            return FeatureFlagKey(value)
        }
    }
}
