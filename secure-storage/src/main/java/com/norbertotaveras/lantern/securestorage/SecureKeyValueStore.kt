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
 * Stores small string values behind validated [SecureStorageKey] instances.
 *
 * Implementations own the persistence backend, encryption policy, and secret-handling guarantees.
 * They should report failures through [SdkResult] instead of throwing for normal read, write,
 * remove, or clear operations.
 */
interface SecureKeyValueStore {
    /**
     * Persists [value] for [key], replacing any existing value for the same key.
     *
     * Empty values are allowed only when the backing implementation configuration permits them.
     */
    suspend fun putString(
        key: SecureStorageKey,
        value: String
    ): SdkResult<Unit>

    /**
     * Returns the stored value for [key], or `null` when the key has no value.
     */
    suspend fun getString(key: SecureStorageKey): SdkResult<String?>

    /**
     * Removes the value for [key].
     *
     * Removing a missing key is treated as a successful no-op by the default implementation.
     */
    suspend fun remove(key: SecureStorageKey): SdkResult<Unit>

    /**
     * Removes every value owned by this store namespace.
     */
    suspend fun clear(): SdkResult<Unit>
}
