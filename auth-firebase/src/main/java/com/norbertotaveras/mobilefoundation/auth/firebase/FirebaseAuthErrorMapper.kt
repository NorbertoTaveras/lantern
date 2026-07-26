package com.norbertotaveras.mobilefoundation.auth.firebase

import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.norbertotaveras.mobilefoundation.core.SdkError

class FirebaseAuthErrorMapper {
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