package com.norbertotaveras.lantern.securestorage

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultSecureTokenStoreTest {

    private val key = SecureStorageKey.unsafe("session.access")

    @Test
    fun `saveToken stores encoded token in key value store`() = runBlockingTest {
        val keyValueStore = InMemorySecureKeyValueStore()
        val tokenStore = DefaultSecureTokenStore(keyValueStore)

        val result = tokenStore.saveToken(
            key = key,
            token = SecureToken(
                value = "abc",
                type = SecureTokenType.Bearer,
                expiresAtEpochMillis = 200L,
                metadata = mapOf("scope" to "profile")
            )
        )

        assertTrue(result is SdkResult.Success)
        assertTrue(keyValueStore.values.getValue(key.value).contains("\"value\":\"abc\""))
    }

    @Test
    fun `getToken decodes stored token`() = runBlockingTest {
        val keyValueStore = InMemorySecureKeyValueStore()
        val tokenStore = DefaultSecureTokenStore(keyValueStore)
        tokenStore.saveToken(
            key = key,
            token = SecureToken(
                value = "abc",
                type = SecureTokenType.RefreshToken,
                expiresAtEpochMillis = 200L,
                metadata = mapOf("scope" to "offline")
            )
        )

        val result = tokenStore.getToken(key)

        assertTrue(result is SdkResult.Success)
        val token = (result as SdkResult.Success).data
        assertEquals("abc", token?.value)
        assertEquals(SecureTokenType.RefreshToken, token?.type)
        assertEquals(200L, token?.expiresAtEpochMillis)
        assertEquals(mapOf("scope" to "offline"), token?.metadata)
    }

    @Test
    fun `getToken returns null when key is not stored`() = runBlockingTest {
        val tokenStore = DefaultSecureTokenStore(InMemorySecureKeyValueStore())

        val result = tokenStore.getToken(key)

        assertTrue(result is SdkResult.Success)
        assertNull((result as SdkResult.Success).data)
    }

    @Test
    fun `getToken returns read failure when stored token cannot be decoded`() = runBlockingTest {
        val keyValueStore = InMemorySecureKeyValueStore(
            values = mutableMapOf(key.value to "not-json")
        )
        val tokenStore = DefaultSecureTokenStore(keyValueStore)

        val result = tokenStore.getToken(key)

        assertTrue(result is SdkResult.Failure)
        assertEquals(SecureStorageErrorCodes.READ_FAILED, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun `removeToken delegates to key value store remove`() = runBlockingTest {
        val keyValueStore = InMemorySecureKeyValueStore(
            values = mutableMapOf(key.value to "stored")
        )
        val tokenStore = DefaultSecureTokenStore(keyValueStore)

        val result = tokenStore.removeToken(key)

        assertTrue(result is SdkResult.Success)
        assertTrue(key.value !in keyValueStore.values)
    }

    @Test
    fun `clearTokens delegates to key value store clear`() = runBlockingTest {
        val keyValueStore = InMemorySecureKeyValueStore(
            values = mutableMapOf(key.value to "stored")
        )
        val tokenStore = DefaultSecureTokenStore(keyValueStore)

        val result = tokenStore.clearTokens()

        assertTrue(result is SdkResult.Success)
        assertTrue(keyValueStore.values.isEmpty())
    }

    @Test
    fun `getToken returns key value store failure`() = runBlockingTest {
        val error = SdkError(
            code = SecureStorageErrorCodes.READ_FAILED,
            message = "Read failed"
        )
        val tokenStore = DefaultSecureTokenStore(FailingSecureKeyValueStore(error))

        val result = tokenStore.getToken(key)

        assertTrue(result is SdkResult.Failure)
        assertSame(error, (result as SdkResult.Failure).error)
    }

    private class InMemorySecureKeyValueStore(
        val values: MutableMap<String, String> = mutableMapOf()
    ) : SecureKeyValueStore {
        override suspend fun putString(
            key: SecureStorageKey,
            value: String
        ): SdkResult<Unit> {
            values[key.value] = value
            return SdkResult.Success(Unit)
        }

        override suspend fun getString(key: SecureStorageKey): SdkResult<String?> {
            return SdkResult.Success(values[key.value])
        }

        override suspend fun remove(key: SecureStorageKey): SdkResult<Unit> {
            values.remove(key.value)
            return SdkResult.Success(Unit)
        }

        override suspend fun clear(): SdkResult<Unit> {
            values.clear()
            return SdkResult.Success(Unit)
        }
    }

    private class FailingSecureKeyValueStore(
        private val error: SdkError
    ) : SecureKeyValueStore {
        override suspend fun putString(
            key: SecureStorageKey,
            value: String
        ): SdkResult<Unit> {
            return SdkResult.Failure(error)
        }

        override suspend fun getString(key: SecureStorageKey): SdkResult<String?> {
            return SdkResult.Failure(error)
        }

        override suspend fun remove(key: SecureStorageKey): SdkResult<Unit> {
            return SdkResult.Failure(error)
        }

        override suspend fun clear(): SdkResult<Unit> {
            return SdkResult.Failure(error)
        }
    }
}
