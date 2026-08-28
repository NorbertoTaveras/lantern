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

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.norbertotaveras.lantern.core.DefaultDispatcherProvider
import com.norbertotaveras.lantern.core.DispatcherProvider
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.logging.NoOpSdkLogger
import com.norbertotaveras.lantern.logging.SdkLogger
import com.norbertotaveras.lantern.securestorage.internal.SecureStorageErrorMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

/**
 * DataStore-backed [SecureKeyValueStore] implementation.
 *
 * This implementation stores app-local values using AndroidX Preferences DataStore. Values are
 * not encrypted by this implementation, so callers should encrypt highly sensitive values before
 * writing them or provide an encrypted [SecureKeyValueStore] implementation.
 *
 * Callers provide Firebase, auth, or app-specific secrets through the public store API; the SDK
 * does not own app credentials or configuration files.
 */
class DataStoreSecureKeyValueStore private constructor(
    context: Context,
    private val config: SecureStorageConfig = SecureStorageConfig(),
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
    private val logger: SdkLogger = NoOpSdkLogger(),
    private val errorMapper: SecureStorageErrorMapper = SecureStorageErrorMapper()
) : SecureKeyValueStore {

    /**
     * Creates a DataStore-backed key-value store using `context.applicationContext`.
     */
    constructor(
        context: Context,
        config: SecureStorageConfig = SecureStorageConfig(),
        dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider,
        logger: SdkLogger = NoOpSdkLogger()
    ) : this(
        context = context,
        config = config,
        dispatcherProvider = dispatcherProvider,
        logger = logger,
        errorMapper = SecureStorageErrorMapper()
    )

    private val dataStore = PreferenceDataStoreFactory.create(
        scope = CoroutineScope(dispatcherProvider.io + SupervisorJob()),
        produceFile = {
            context.applicationContext.preferencesDataStoreFile(config.fileName())
        }
    )

    override suspend fun putString(
        key: SecureStorageKey,
        value: String
    ): SdkResult<Unit> {
        if (!config.allowEmptyValues && value.isEmpty()) {
            return SdkResult.Failure(emptyValueError())
        }

        return runStorageOperation(
            operation = SecureStorageErrorMapper.Operation.Write,
            failureMessage = "Unable to write secure storage value."
        ) {
            dataStore.edit { preferences ->
                preferences[key.preferenceKey()] = value
            }
        }
    }

    override suspend fun getString(key: SecureStorageKey): SdkResult<String?> {
        return runStorageOperation(
            operation = SecureStorageErrorMapper.Operation.Read,
            failureMessage = "Unable to read secure storage value."
        ) {
            dataStore.data.first()[key.preferenceKey()]
        }
    }

    override suspend fun remove(key: SecureStorageKey): SdkResult<Unit> {
        return runStorageOperation(
            operation = SecureStorageErrorMapper.Operation.Remove,
            failureMessage = "Unable to remove secure storage value."
        ) {
            dataStore.edit { preferences ->
                preferences.remove(key.preferenceKey())
            }
        }
    }

    override suspend fun clear(): SdkResult<Unit> {
        return runStorageOperation(
            operation = SecureStorageErrorMapper.Operation.Clear,
            failureMessage = "Unable to clear secure storage values."
        ) {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    private suspend fun <T> runStorageOperation(
        operation: SecureStorageErrorMapper.Operation,
        failureMessage: String,
        block: suspend () -> T
    ): SdkResult<T> {
        return try {
            SdkResult.Success(block())
        } catch (throwable: Throwable) {
            logger.error(failureMessage, throwable)
            SdkResult.Failure(errorMapper.map(operation, throwable))
        }
    }

    private fun SecureStorageKey.preferenceKey(): Preferences.Key<String> {
        return stringPreferencesKey(value)
    }

    private fun SecureStorageConfig.fileName(): String {
        return "${namespace.trim()}.preferences_pb"
    }

    private fun emptyValueError(): SdkError {
        return SdkError(
            code = SecureStorageErrorCodes.WRITE_FAILED,
            message = "Secure storage config does not allow empty values."
        )
    }
}
