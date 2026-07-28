package com.norbertotaveras.mobilefoundation.securestorage

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface SecureTokenStore {
    suspend fun saveToken(
        key: SecureStorageKey,
        token: SecureToken
    ): SdkResult<Unit>

    suspend fun getToken(key: SecureStorageKey): SdkResult<SecureToken?>

    suspend fun removeToken(key: SecureStorageKey): SdkResult<Unit>

    suspend fun clearTokens(): SdkResult<Unit>
}
