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

package com.norbertotaveras.lantern.auth.firebase.internal

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.norbertotaveras.lantern.auth.firebase.FirebaseAuthErrorCodes
import com.norbertotaveras.lantern.core.SdkError

internal class FirebaseAuthErrorMapper {
    fun map(throwable: Throwable): SdkError {
        return when (throwable) {
            is FirebaseAuthInvalidCredentialsException -> {
                SdkError(
                    code = FirebaseAuthErrorCodes.INVALID_CREDENTIALS,
                    message = throwable.localizedMessage ?: "Invalid Firebase authentication credentials.",
                    cause = throwable
                )
            }

            is FirebaseAuthInvalidUserException -> {
                SdkError(
                    code = FirebaseAuthErrorCodes.USER_NOT_FOUND,
                    message = throwable.localizedMessage ?: "Firebase user was not found.",
                    cause = throwable
                )
            }

            is FirebaseAuthUserCollisionException -> {
                SdkError(
                    code = FirebaseAuthErrorCodes.EMAIL_ALREADY_IN_USE,
                    message = throwable.localizedMessage ?: "Firebase account already exists.",
                    cause = throwable
                )
            }

            is FirebaseAuthWeakPasswordException -> {
                SdkError(
                    code = FirebaseAuthErrorCodes.WEAK_PASSWORD,
                    message = throwable.localizedMessage ?: "Firebase password is too weak.",
                    cause = throwable
                )
            }

            is FirebaseNetworkException -> {
                SdkError(
                    code = FirebaseAuthErrorCodes.NETWORK_ERROR,
                    message = throwable.localizedMessage ?: "Firebase authentication network error.",
                    cause = throwable
                )
            }

            else -> {
                SdkError(
                    code = FirebaseAuthErrorCodes.UNKNOWN,
                    message = throwable.localizedMessage ?: "Unknown Firebase authentication error.",
                    cause = throwable
                )
            }
        }
    }
}
