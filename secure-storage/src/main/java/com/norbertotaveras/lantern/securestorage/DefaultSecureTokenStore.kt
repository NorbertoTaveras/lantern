package com.norbertotaveras.lantern.securestorage

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.internal.SecureTokenCodec

/**
 * Default [SecureTokenStore] implementation backed by a [SecureKeyValueStore].
 *
 * Tokens are encoded as structured strings before persistence and decoded when read back. This
 * class does not encrypt token values; encryption is determined by the supplied [SecureKeyValueStore].
 */
class DefaultSecureTokenStore private constructor(
    private val keyValueStore: SecureKeyValueStore,
    private val tokenCodec: SecureTokenCodec = SecureTokenCodec()
) : SecureTokenStore {

    /**
     * Creates a token store using [keyValueStore] for persistence.
     */
    constructor(
        keyValueStore: SecureKeyValueStore
    ) : this(
        keyValueStore = keyValueStore,
        tokenCodec = SecureTokenCodec()
    )

    override suspend fun saveToken(
        key: SecureStorageKey,
        token: SecureToken
    ): SdkResult<Unit> {
        return when (val encodedToken = tokenCodec.encode(token)) {
            is SdkResult.Success -> keyValueStore.putString(key, encodedToken.data)
            is SdkResult.Failure -> encodedToken
        }
    }

    override suspend fun getToken(key: SecureStorageKey): SdkResult<SecureToken?> {
        return when (val storedToken = keyValueStore.getString(key)) {
            is SdkResult.Success -> {
                val value = storedToken.data
                if (value == null) {
                    SdkResult.Success(null)
                } else {
                    tokenCodec.decode(value)
                }
            }
            is SdkResult.Failure -> storedToken
        }
    }

    override suspend fun removeToken(key: SecureStorageKey): SdkResult<Unit> {
        return keyValueStore.remove(key)
    }

    override suspend fun clearTokens(): SdkResult<Unit> {
        return keyValueStore.clear()
    }
}
