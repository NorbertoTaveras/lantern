package com.norbertotaveras.lantern.notifications.firebase

import com.norbertotaveras.lantern.core.SdkError

internal class FirebaseMessagingErrorMapper {

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
        GetToken(
            errorCode = FirebaseMessagingErrorCodes.TOKEN_UNAVAILABLE,
            fallbackMessage = "Unable to register Firebase Messaging and retrieve the FCM token."
        ),
        DeleteToken(
            errorCode = FirebaseMessagingErrorCodes.UNREGISTER_FAILED,
            fallbackMessage = "Unable to unregister Firebase Messaging."
        ),
        SubscribeTopic(
            errorCode = FirebaseMessagingErrorCodes.TOPIC_SUBSCRIPTION_FAILED,
            fallbackMessage = "Unable to subscribe to Firebase Messaging topic."
        ),
        UnsubscribeTopic(
            errorCode = FirebaseMessagingErrorCodes.TOPIC_UNSUBSCRIPTION_FAILED,
            fallbackMessage = "Unable to unsubscribe from Firebase Messaging topic."
        ),
        Unknown(
            errorCode = FirebaseMessagingErrorCodes.UNKNOWN,
            fallbackMessage = "Unknown Firebase Messaging error."
        )
    }
}
