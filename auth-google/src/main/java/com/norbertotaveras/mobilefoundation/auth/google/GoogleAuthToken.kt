package com.norbertotaveras.mobilefoundation.auth.google

/**
 * Google ID token credential returned by [GoogleAuthProvider].
 */
data class GoogleAuthToken(
    val idToken: String,
    val displayName: String? = null,
    val email: String? = null,
    val profilePictureUri: String? = null
)
