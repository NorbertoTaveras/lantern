package com.norbertotaveras.lantern.auth.firebase

/**
 * Feature flags for Firebase Auth provider capabilities.
 */
data class FirebaseAuthConfig(
    val enableAnonymousAuth: Boolean = true,
    val enableEmailPasswordAuth: Boolean = true
)
