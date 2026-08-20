package com.norbertotaveras.mobilefoundation.auth.firebasegoogle

/**
 * Configuration for Google sign-in bridged into Firebase Authentication.
 */
data class FirebaseGoogleAuthConfig(
    val serverClientId: String,
    val filterByAuthorizedAccounts: Boolean = true,
    val autoSelectEnabled: Boolean = true
)
