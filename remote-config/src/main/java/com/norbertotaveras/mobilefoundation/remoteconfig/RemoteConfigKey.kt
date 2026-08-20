package com.norbertotaveras.mobilefoundation.remoteconfig

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.remoteconfig.internal.RemoteConfigKeyValidator

/**
 * Validated key for remote config values.
 *
 * Prefer [from] for dynamic values so invalid keys are returned as [SdkResult.Failure].
 * [unsafe] is intended for trusted constants.
 */
@JvmInline
value class RemoteConfigKey private constructor(val value: String) {
    companion object {
        /**
         * Creates a [RemoteConfigKey] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<RemoteConfigKey> {
            return when (val result = RemoteConfigKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(RemoteConfigKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [RemoteConfigKey] without validation.
         */
        fun unsafe(value: String): RemoteConfigKey {
            return RemoteConfigKey(value)
        }
    }
}
