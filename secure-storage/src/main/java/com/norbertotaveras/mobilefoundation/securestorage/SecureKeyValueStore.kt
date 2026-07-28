package com.norbertotaveras.mobilefoundation.securestorage

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface SecureKeyValueStore {
    suspend fun putString(
        key: SecureStorageKey,
        value: String
    ): SdkResult<Unit>

    suspend fun getString(key: SecureStorageKey): SdkResult<String?>

    suspend fun remove(key: SecureStorageKey): SdkResult<Unit>

    suspend fun clear(): SdkResult<Unit>
}
