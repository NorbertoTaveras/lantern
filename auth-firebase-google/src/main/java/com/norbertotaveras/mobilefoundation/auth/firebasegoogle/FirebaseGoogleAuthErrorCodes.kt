package com.norbertotaveras.mobilefoundation.auth.firebasegoogle

/**
 * Stable error codes returned by the Firebase + Google auth bridge.
 */
object FirebaseGoogleAuthErrorCodes {
    const val GOOGLE_SIGN_IN_FAILED = "firebase_google_sign_in_failed"
    const val FIREBASE_SIGN_IN_FAILED = "firebase_google_firebase_sign_in_failed"
    const val SIGN_OUT_FAILED = "firebase_google_sign_out_failed"
    const val SESSION_LOOKUP_FAILED = "firebase_google_session_lookup_failed"
    const val SESSION_NOT_FOUND = "firebase_google_session_not_found"
}
