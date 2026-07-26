package com.norbertotaveras.mobilefoundation.auth.google

import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.norbertotaveras.mobilefoundation.core.SdkError

class GoogleAuthErrorMapper {

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
                    message = throwable.localizedMessage ?: "Unable to get Google credential.",
                    cause = throwable
                )
            }

            is ClearCredentialException -> {
                SdkError(
                    code = GoogleAuthErrorCodes.CLEAR_CREDENTIAL_FAILED,
                    message = throwable.localizedMessage ?: "Unable to clear Google credential state.",
                    cause = throwable
                )
            }

            else -> {
                SdkError(
                    code = GoogleAuthErrorCodes.UNKNOWN,
                    message = throwable.localizedMessage ?: "Unknown Google authentication error.",
                    cause = throwable
                )
            }
        }
    }
}