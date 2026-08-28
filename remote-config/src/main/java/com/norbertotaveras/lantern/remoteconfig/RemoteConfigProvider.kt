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

import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.flow.Flow

/**
 * Provider-neutral remote config contract.
 *
 * Implementations may be backed by Firebase Remote Config, static in-memory values, or another
 * provider, but should expose values through [RemoteConfigValue] and failures through [SdkResult].
 */
interface RemoteConfigProvider {
    /**
     * Emits snapshots when the active config values change.
     */
    val updates: Flow<RemoteConfigSnapshot>

    /**
     * Applies default values used before provider values are fetched or activated.
     */
    suspend fun setDefaults(defaults: RemoteConfigDefaults): SdkResult<Unit>

    /**
     * Fetches the latest provider values without necessarily making them active.
     */
    suspend fun fetch(): SdkResult<RemoteConfigFetchStatus>

    /**
     * Activates the latest fetched values.
     *
     * Returns `true` when activation changed the active value set.
     */
    suspend fun activate(): SdkResult<Boolean>

    /**
     * Fetches and then activates values using the backing provider's semantics.
     */
    suspend fun fetchAndActivate(): SdkResult<Boolean> {
        return when (val fetchResult = fetch()) {
            is SdkResult.Failure -> fetchResult
            is SdkResult.Success -> activate()
        }
    }

    /**
     * Returns the active value for [key].
     */
    suspend fun getValue(key: RemoteConfigKey): SdkResult<RemoteConfigValue>

    /**
     * Returns a point-in-time snapshot of active values.
     */
    suspend fun getSnapshot(): SdkResult<RemoteConfigSnapshot>
}
