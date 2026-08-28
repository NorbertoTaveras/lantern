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

/**
 * Structured token value persisted by [SecureTokenStore].
 *
 * [value] must not be blank. [expiresAtEpochMillis] is optional because not every credential
 * has an SDK-visible expiration. [metadata] is intended for lightweight provider/session
 * attributes and should not be used as a general document store.
 */
data class SecureToken(
    /**
     * Raw credential or token string.
     */
    val value: String,
    /**
     * Token category used by callers to distinguish access, ID, refresh, or custom tokens.
     */
    val type: SecureTokenType = SecureTokenType.Bearer,
    /**
     * Optional absolute expiration time in epoch milliseconds.
     */
    val expiresAtEpochMillis: Long? = null,
    /**
     * Optional lightweight token metadata.
     */
    val metadata: Map<String, String> = emptyMap()
) {
    init {
        require(value.isNotBlank()) { "Secure token value cannot be blank." }
    }

    /**
     * Returns `true` when [currentTimeMillis] is at or after [expiresAtEpochMillis].
     *
     * Tokens without an expiration are treated as not expired.
     */
    fun isExpired(currentTimeMillis: Long): Boolean {
        return expiresAtEpochMillis?.let { currentTimeMillis >= it } ?: false
    }
}
