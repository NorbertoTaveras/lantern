package com.norbertotaveras.lantern.auth.google

/**
 * Stable error codes returned by Google auth integration.
 */
object GoogleAuthErrorCodes {
    const val UNKNOWN = "google_auth_unknown"
    const val USER_CANCELLED = "google_auth_user_cancelled"
    const val NO_CREDENTIAL = "google_auth_no_credential"
    const val INVALID_CREDENTIAL = "google_auth_invalid_credential"
    const val CLEAR_CREDENTIAL_FAILED = "google_auth_clear_credential_failed"
}
