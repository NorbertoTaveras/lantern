package com.norbertotaveras.mobilefoundation.featureflags.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.featureflags.FeatureFlagErrorCodes

internal object FeatureFlagKeyValidator {
    private val allowedPattern = Regex("^[A-Za-z][A-Za-z0-9_.-]{0,127}$")

    fun validate(value: String): SdkResult<String> {
        val normalized = value.trim()
        if (!allowedPattern.matches(normalized)) {
            return SdkResult.Failure(
                SdkError(
                    code = FeatureFlagErrorCodes.INVALID_KEY,
                    message = "Feature flag keys must start with a letter and contain only letters, numbers, underscores, dots, or hyphens."
                )
            )
        }

        return SdkResult.Success(normalized)
    }
}
