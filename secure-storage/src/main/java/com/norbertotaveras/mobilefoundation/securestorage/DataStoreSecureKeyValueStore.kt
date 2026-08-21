package com.norbertotaveras.mobilefoundation.securestorage

import android.content.Context
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import com.norbertotaveras.mobilefoundation.core.DefaultDispatcherProvider
import com.norbertotaveras.mobilefoundation.core.DispatcherProvider
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.logging.NoOpSdkLogger
import com.norbertotaveras.mobilefoundation.logging.SdkLogger
import com.norbertotaveras.mobilefoundation.securestorage.internal.SecureStorageErrorMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first

/**
 * DataStore-backed [SecureKeyValueStore] implementation.
 *
 * This implementation stores values in the app process using AndroidX Preferences DataStore.
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
