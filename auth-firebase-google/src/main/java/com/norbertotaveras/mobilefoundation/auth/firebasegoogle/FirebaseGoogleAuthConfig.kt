package com.norbertotaveras.mobilefoundation.auth.firebasegoogle

data class FirebaseGoogleAuthConfig(
    val serverClientId: String,
    val filterByAuthorizedAccounts: Boolean = true,
    val autoSelectEnabled: Boolean = true
)