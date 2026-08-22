package com.norbertotaveras.lantern.securestorage

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.internal.StorageKeyValidator

/**
 * Validated key used by secure storage APIs.
 *
 * Prefer [from] for user-provided or dynamic values so invalid keys are reported as
 * [SdkResult.Failure]. [unsafe] exists for trusted constants and bypasses validation.
 */
@JvmInline
value class SecureStorageKey private constructor(val value: String) {
    companion object {
        /**
         * Creates a [SecureStorageKey] after trimming and validating [value].
         *
         * Valid keys must be non-blank, at most 128 characters, and contain only letters,
         * numbers, periods, underscores, colons, and hyphens.
         */
        @JvmStatic
        fun from(value: String): SdkResult<SecureStorageKey> {
            return when (val result = StorageKeyValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(SecureStorageKey(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [SecureStorageKey] without validation.
         *
         * Use this only for trusted compile-time constants that already follow the documented
         * key format.
         */
        fun unsafe(value: String): SecureStorageKey {
            return SecureStorageKey(value)
        }
    }
}
