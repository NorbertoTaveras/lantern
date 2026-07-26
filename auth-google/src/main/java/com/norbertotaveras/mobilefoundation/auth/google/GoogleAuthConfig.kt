package com.norbertotaveras.mobilefoundation.auth.google

data class GoogleAuthConfig(
    val serverClientId: String,
    val filterByAuthorizedAccounts: Boolean = true,
    val autoSelectEnabled: Boolean = true
)