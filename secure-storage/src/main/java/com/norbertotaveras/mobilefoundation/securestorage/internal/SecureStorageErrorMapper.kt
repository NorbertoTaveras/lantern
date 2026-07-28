package com.norbertotaveras.mobilefoundation.securestorage.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.securestorage.SecureStorageErrorCodes

class SecureStorageErrorMapper {

    fun map(
        operation: Operation,
        throwable: Throwable
    ): SdkError {
        return SdkError(
            code = operation.errorCode,
            message = throwable.localizedMessage ?: operation.fallbackMessage,
            cause = throwable
        )
    }

    enum class Operation(
        val errorCode: String,
        val fallbackMessage: String
    ) {
        Read(
            errorCode = SecureStorageErrorCodes.READ_FAILED,
            fallbackMessage = "Unable to read secure storage value."
        ),
        Write(
            errorCode = SecureStorageErrorCodes.WRITE_FAILED,
            fallbackMessage = "Unable to write secure storage value."
        ),
        Remove(
            errorCode = SecureStorageErrorCodes.REMOVE_FAILED,
            fallbackMessage = "Unable to remove secure storage value."
        ),
        Clear(
            errorCode = SecureStorageErrorCodes.CLEAR_FAILED,
            fallbackMessage = "Unable to clear secure storage values."
        )
    }
}
