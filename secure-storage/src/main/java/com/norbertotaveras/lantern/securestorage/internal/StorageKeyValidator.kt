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

package com.norbertotaveras.lantern.securestorage.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.SecureStorageErrorCodes

internal object StorageKeyValidator {
    private const val MAX_KEY_LENGTH = 128
    private val keyPattern = Regex("^[A-Za-z0-9._:-]+$")

    fun validate(key: String): SdkResult<String> {
        val normalizedKey = key.trim()
        return when {
            normalizedKey.isEmpty() -> invalid("Storage key cannot be blank.")
            normalizedKey.length > MAX_KEY_LENGTH -> invalid("Storage key cannot exceed $MAX_KEY_LENGTH characters.")
            !keyPattern.matches(normalizedKey) -> invalid(
                "Storage key can only contain letters, numbers, periods, underscores, colons, and hyphens."
            )
            else -> SdkResult.Success(normalizedKey)
        }
    }

    private fun invalid(message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = SecureStorageErrorCodes.INVALID_KEY,
                message = message
            )
        )
    }
}
