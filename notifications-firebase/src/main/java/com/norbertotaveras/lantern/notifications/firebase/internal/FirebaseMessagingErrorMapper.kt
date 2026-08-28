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

package com.norbertotaveras.lantern.notifications.firebase.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.notifications.firebase.FirebaseMessagingErrorCodes

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
