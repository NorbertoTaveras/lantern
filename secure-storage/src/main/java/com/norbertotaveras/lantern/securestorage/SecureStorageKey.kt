/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
