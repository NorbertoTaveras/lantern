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

package com.norbertotaveras.lantern.remoteconfig.firebase.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.remoteconfig.firebase.FirebaseRemoteConfigErrorCodes

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
