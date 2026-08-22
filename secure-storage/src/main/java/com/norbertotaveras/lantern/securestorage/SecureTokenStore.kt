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
