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

package com.norbertotaveras.lantern.remoteconfig

/**
 * Point-in-time view of active remote config values.
 */
data class RemoteConfigSnapshot(
    /**
     * Active values keyed by [RemoteConfigKey].
     */
    val values: Map<RemoteConfigKey, RemoteConfigValue>,
    /**
     * Most recent fetch status when known.
     */
    val fetchStatus: RemoteConfigFetchStatus? = null,
    /**
     * Epoch time when values were last activated, when known.
     */
    val activatedAtMillis: Long? = null
) {
    /**
     * Returns the value for [key], or `null` when the snapshot does not contain it.
     */
    fun valueFor(key: RemoteConfigKey): RemoteConfigValue? {
        return values[key]
    }

    companion object {
        /**
         * Empty snapshot used before values are available.
         */
        val Empty = RemoteConfigSnapshot(values = emptyMap())
    }
}
