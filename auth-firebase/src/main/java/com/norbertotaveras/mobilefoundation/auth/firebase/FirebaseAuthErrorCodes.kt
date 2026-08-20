package com.norbertotaveras.mobilefoundation.auth.firebase

/**
 * Stable error codes returned by Firebase Auth integration.
 */
object FirebaseAuthErrorCodes {
    const val UNKNOWN = "firebase_auth_unknown"
    const val SESSION_NOT_FOUND = "firebase_auth_session_not_found"
    const val INVALID_CREDENTIALS = "firebase_auth_invalid_credentials"
    const val USER_NOT_FOUND = "firebase_auth_user_not_found"
    const val EMAIL_ALREADY_IN_USE = "firebase_auth_email_already_in_use"
    const val WEAK_PASSWORD = "firebase_auth_weak_password"
    const val NETWORK_ERROR = "firebase_auth_network_error"
}
