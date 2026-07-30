package com.norbertotaveras.mobilefoundation.remoteconfig.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigErrorCodes

internal object RemoteConfigKeyValidator {
    private val allowedPattern = Regex("^[A-Za-z][A-Za-z0-9_]{0,127}$")

    fun validate(value: String): SdkResult<String> {
        val normalized = value.trim()
        if (!allowedPattern.matches(normalized)) {
            return SdkResult.Failure(
                SdkError(
                    code = RemoteConfigErrorCodes.INVALID_KEY,
                    message = "Remote config keys must start with a letter and contain only letters, numbers, or underscores."
                )
            )
        }

        return SdkResult.Success(normalized)
    }
}
