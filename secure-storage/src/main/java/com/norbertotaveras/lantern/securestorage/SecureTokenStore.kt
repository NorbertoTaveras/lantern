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

/**
 * Stores structured token values using [SecureStorageKey] lookup keys.
 *
 * Token stores are intended for app session tokens, provider ID tokens, refresh tokens, and
 * other short structured credentials that need consistent encoding and error mapping. The backing
 * [SecureKeyValueStore] owns persistence security, including whether values are encrypted.
 */
interface SecureTokenStore {
    /**
     * Encodes and persists [token] for [key], replacing any previous token at that key.
     */
    suspend fun saveToken(
        key: SecureStorageKey,
        token: SecureToken
    ): SdkResult<Unit>

    /**
     * Returns the decoded token for [key], or `null` when no token is stored.
     */
    suspend fun getToken(key: SecureStorageKey): SdkResult<SecureToken?>

    /**
     * Removes the token stored at [key].
     */
    suspend fun removeToken(key: SecureStorageKey): SdkResult<Unit>

    /**
     * Removes every token value owned by the backing store.
     */
    suspend fun clearTokens(): SdkResult<Unit>
}
