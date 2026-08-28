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
 * Configuration for the default secure storage implementation.
 *
 * [namespace] determines the storage file owned by this SDK store. Use a stable namespace per
 * logical store so unrelated app data is not cleared together. [allowEmptyValues] controls
 * whether empty strings can be persisted through [SecureKeyValueStore.putString].
 */
data class SecureStorageConfig(
    /**
     * Stable storage namespace used to derive the DataStore file name.
     *
     * Namespaces are trimmed, must be non-blank, and may contain letters, numbers, periods,
     * underscores, and hyphens.
     */
    val namespace: String = DEFAULT_NAMESPACE,
    /**
     * Whether [SecureKeyValueStore.putString] accepts empty string values.
     */
    val allowEmptyValues: Boolean = true
) {
    init {
        val normalizedNamespace = namespace.trim()
        require(normalizedNamespace.isNotEmpty()) { "Secure storage namespace cannot be blank." }
        require(normalizedNamespace.length <= MAX_NAMESPACE_LENGTH) {
            "Secure storage namespace cannot exceed $MAX_NAMESPACE_LENGTH characters."
        }
        require(NAMESPACE_PATTERN.matches(normalizedNamespace)) {
            "Secure storage namespace can only contain letters, numbers, periods, underscores, and hyphens."
        }
    }

    companion object {
        /**
         * Default namespace used when a store does not need app-specific separation.
         */
        const val DEFAULT_NAMESPACE = "lantern_secure_storage"

        /**
         * Namespace used by Lantern 0.1.0 before the SDK rename was fully reflected in storage.
         *
         * Use this value when an app needs to keep reading data written by the 0.1.0 default
         * DataStore-backed implementation.
         */
        const val LEGACY_MOBILE_FOUNDATION_NAMESPACE = "mobile_foundation_secure_storage"

        private const val MAX_NAMESPACE_LENGTH = 80
        private val NAMESPACE_PATTERN = Regex("^[A-Za-z0-9._-]+$")
    }
}
