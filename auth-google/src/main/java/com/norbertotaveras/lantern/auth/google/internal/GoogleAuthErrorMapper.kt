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

package com.norbertotaveras.lantern.auth.google.internal

import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.norbertotaveras.lantern.auth.google.GoogleAuthErrorCodes
import com.norbertotaveras.lantern.core.SdkError

internal class GoogleAuthErrorMapper {

    fun map(throwable: Throwable): SdkError {
        return when (throwable) {
            is GetCredentialCancellationException -> {
                SdkError(
                    code = GoogleAuthErrorCodes.USER_CANCELLED,
                    message = "Google sign-in was cancelled.",
                    cause = throwable
                )
            }

            is NoCredentialException -> {
                SdkError(
                    code = GoogleAuthErrorCodes.NO_CREDENTIAL,
                    message = "No Google credential was available.",
                    cause = throwable
                )
            }

            is GetCredentialException -> {
                SdkError(
                    code = GoogleAuthErrorCodes.INVALID_CREDENTIAL,
                    message = "Unable to get Google credential.",
                    cause = throwable
                )
            }

            is ClearCredentialException -> {
                SdkError(
                    code = GoogleAuthErrorCodes.CLEAR_CREDENTIAL_FAILED,
                    message = "Unable to clear Google credential state.",
                    cause = throwable
                )
            }

            else -> {
                SdkError(
                    code = GoogleAuthErrorCodes.UNKNOWN,
                    message = "Unknown Google authentication error.",
                    cause = throwable
                )
            }
        }
    }
}
