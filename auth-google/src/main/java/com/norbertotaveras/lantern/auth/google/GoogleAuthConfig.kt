package com.norbertotaveras.lantern.auth.google

/**
 * Configuration for Google sign-in through Credential Manager.
 */
data class GoogleAuthConfig(
    val serverClientId: String,
    val filterByAuthorizedAccounts: Boolean = true,
    val autoSelectEnabled: Boolean = true
)
