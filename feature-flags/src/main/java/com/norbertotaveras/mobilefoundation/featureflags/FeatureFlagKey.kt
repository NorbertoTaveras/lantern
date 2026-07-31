package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.featureflags.internal.FeatureFlagKeyValidator

@JvmInline
value class FeatureFlagKey private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<FeatureFlagKey> {
            return when (val result = FeatureFlagKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(FeatureFlagKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): FeatureFlagKey {
            return FeatureFlagKey(value)
        }
    }
}
