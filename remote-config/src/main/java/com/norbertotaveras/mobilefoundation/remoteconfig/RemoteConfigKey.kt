package com.norbertotaveras.mobilefoundation.remoteconfig

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.remoteconfig.internal.RemoteConfigKeyValidator

@JvmInline
value class RemoteConfigKey private constructor(val value: String) {
    companion object {
        @JvmStatic
        fun from(value: String): SdkResult<RemoteConfigKey> {
            return when (val result = RemoteConfigKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(RemoteConfigKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): RemoteConfigKey {
            return RemoteConfigKey(value)
        }
    }
}
