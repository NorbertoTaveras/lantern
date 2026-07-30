package com.norbertotaveras.mobilefoundation.remoteconfig

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

interface RemoteConfigProvider {
    val updates: Flow<RemoteConfigSnapshot>

    suspend fun setDefaults(defaults: RemoteConfigDefaults): SdkResult<Unit>

    suspend fun fetch(): SdkResult<RemoteConfigFetchStatus>

    suspend fun activate(): SdkResult<Boolean>

    suspend fun fetchAndActivate(): SdkResult<Boolean> {
        return when (val fetchResult = fetch()) {
            is SdkResult.Failure -> fetchResult
            is SdkResult.Success -> activate()
        }
    }

    suspend fun getValue(key: RemoteConfigKey): SdkResult<RemoteConfigValue>

    suspend fun getSnapshot(): SdkResult<RemoteConfigSnapshot>
}
