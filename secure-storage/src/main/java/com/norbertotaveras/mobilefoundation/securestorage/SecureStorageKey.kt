package com.norbertotaveras.mobilefoundation.securestorage

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.securestorage.internal.StorageKeyValidator

@JvmInline
value class SecureStorageKey private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<SecureStorageKey> {
            return when (val result = StorageKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(SecureStorageKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): SecureStorageKey {
            return SecureStorageKey(value)
        }
    }
}
