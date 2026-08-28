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

package com.norbertotaveras.lantern.remoteconfig.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS
import com.google.firebase.remoteconfig.FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigErrorCodes
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigFetchStatus
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigSnapshot
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.firebase.internal.FirebaseRemoteConfigErrorMapper
import com.norbertotaveras.lantern.remoteconfig.firebase.internal.FirebaseRemoteConfigValueMapper
import com.norbertotaveras.lantern.remoteconfig.firebase.internal.toFirebase
import com.norbertotaveras.lantern.remoteconfig.firebase.internal.toFirebaseDefaults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await

/**
 * [RemoteConfigProvider] implementation backed by Firebase Remote Config.
 */
class FirebaseRemoteConfigProvider private constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance(),
    private val config: FirebaseRemoteConfigProviderConfig = FirebaseRemoteConfigProviderConfig(),
    private val valueMapper: FirebaseRemoteConfigValueMapper = FirebaseRemoteConfigValueMapper(config.valueTypes),
    private val errorMapper: FirebaseRemoteConfigErrorMapper = FirebaseRemoteConfigErrorMapper()
) : RemoteConfigProvider {

    /**
     * Creates a Firebase Remote Config provider with default Firebase instance and settings.
     */
    constructor() : this(
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance(),
        config = FirebaseRemoteConfigProviderConfig()
    )

    /**
     * Creates a Firebase Remote Config provider with injectable Firebase dependencies.
     */
    constructor(
        firebaseRemoteConfig: FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance(),
        config: FirebaseRemoteConfigProviderConfig = FirebaseRemoteConfigProviderConfig()
    ) : this(
        firebaseRemoteConfig = firebaseRemoteConfig,
        config = config,
        valueMapper = FirebaseRemoteConfigValueMapper(config.valueTypes),
        errorMapper = FirebaseRemoteConfigErrorMapper()
    )

    private val snapshotState = MutableStateFlow(RemoteConfigSnapshot.Empty)

    override val updates: Flow<RemoteConfigSnapshot> = snapshotState.asStateFlow()

    /**
     * Applies configured Firebase Remote Config settings.
     */
    suspend fun applySettings(): SdkResult<Unit> {
        return try {
            firebaseRemoteConfig.setConfigSettingsAsync(config.settings.toFirebase()).await()
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseRemoteConfigErrorMapper.Operation.Settings, throwable))
        }
    }

    override suspend fun setDefaults(defaults: RemoteConfigDefaults): SdkResult<Unit> {
        return try {
            firebaseRemoteConfig.setDefaultsAsync(defaults.toFirebaseDefaults()).await()
            refreshSnapshot(fetchStatus = null)
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseRemoteConfigErrorMapper.Operation.Defaults, throwable))
        }
    }

    override suspend fun fetch(): SdkResult<RemoteConfigFetchStatus> {
        return try {
            firebaseRemoteConfig.fetch().await()
            val status = firebaseRemoteConfig.info.lastFetchStatus.toRemoteConfigFetchStatus()
            refreshSnapshot(fetchStatus = status)
            SdkResult.Success(status)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseRemoteConfigErrorMapper.Operation.Fetch, throwable))
        }
    }

    override suspend fun activate(): SdkResult<Boolean> {
        return try {
            val activated = firebaseRemoteConfig.activate().await()
            refreshSnapshot(fetchStatus = firebaseRemoteConfig.info.lastFetchStatus.toRemoteConfigFetchStatus())
            SdkResult.Success(activated)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseRemoteConfigErrorMapper.Operation.Activate, throwable))
        }
    }

    override suspend fun fetchAndActivate(): SdkResult<Boolean> {
        return try {
            val activated = firebaseRemoteConfig.fetchAndActivate().await()
            refreshSnapshot(fetchStatus = firebaseRemoteConfig.info.lastFetchStatus.toRemoteConfigFetchStatus())
            SdkResult.Success(activated)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(FirebaseRemoteConfigErrorMapper.Operation.Fetch, throwable))
        }
    }

    override suspend fun getValue(key: RemoteConfigKey): SdkResult<RemoteConfigValue> {
        val value = firebaseRemoteConfig.getValue(key.value)
        if (value.source == FirebaseRemoteConfig.VALUE_SOURCE_STATIC) {
            return SdkResult.Failure(
                SdkError(
                    code = RemoteConfigErrorCodes.VALUE_NOT_FOUND,
                    message = "Remote config value '${key.value}' was not found."
                )
            )
        }

        return SdkResult.Success(valueMapper.map(key, value))
    }

    override suspend fun getSnapshot(): SdkResult<RemoteConfigSnapshot> {
        return SdkResult.Success(currentSnapshot(fetchStatus = firebaseRemoteConfig.info.lastFetchStatus.toRemoteConfigFetchStatus()))
    }

    private fun refreshSnapshot(fetchStatus: RemoteConfigFetchStatus?) {
        snapshotState.value = currentSnapshot(fetchStatus)
    }

    private fun currentSnapshot(fetchStatus: RemoteConfigFetchStatus?): RemoteConfigSnapshot {
        val values = firebaseRemoteConfig.getAll()
            .mapKeys { (key, _) -> RemoteConfigKey.unsafe(key) }
            .mapValues { (key, value) -> valueMapper.map(key, value) }

        return RemoteConfigSnapshot(
            values = values,
            fetchStatus = fetchStatus,
            activatedAtMillis = System.currentTimeMillis()
        )
    }

    private fun Int.toRemoteConfigFetchStatus(): RemoteConfigFetchStatus {
        return when (this) {
            LAST_FETCH_STATUS_SUCCESS -> RemoteConfigFetchStatus.Success
            LAST_FETCH_STATUS_THROTTLED -> RemoteConfigFetchStatus.Throttled
            else -> RemoteConfigFetchStatus.NoChange
        }
    }
}
