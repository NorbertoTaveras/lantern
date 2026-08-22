package com.norbertotaveras.lantern.remoteconfig.firebase

import com.norbertotaveras.lantern.core.SdkError

internal class FirebaseRemoteConfigErrorMapper {

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
        Fetch(
            errorCode = FirebaseRemoteConfigErrorCodes.FETCH_FAILED,
            fallbackMessage = "Unable to fetch Firebase Remote Config values."
        ),
        Activate(
            errorCode = FirebaseRemoteConfigErrorCodes.ACTIVATE_FAILED,
            fallbackMessage = "Unable to activate Firebase Remote Config values."
        ),
        Defaults(
            errorCode = FirebaseRemoteConfigErrorCodes.DEFAULTS_FAILED,
            fallbackMessage = "Unable to set Firebase Remote Config defaults."
        ),
        Settings(
            errorCode = FirebaseRemoteConfigErrorCodes.SETTINGS_FAILED,
            fallbackMessage = "Unable to apply Firebase Remote Config settings."
        ),
        Value(
            errorCode = FirebaseRemoteConfigErrorCodes.VALUE_NOT_FOUND,
            fallbackMessage = "Firebase Remote Config value was not found."
        ),
        Unknown(
            errorCode = FirebaseRemoteConfigErrorCodes.UNKNOWN,
            fallbackMessage = "Unknown Firebase Remote Config error."
        )
    }
}
