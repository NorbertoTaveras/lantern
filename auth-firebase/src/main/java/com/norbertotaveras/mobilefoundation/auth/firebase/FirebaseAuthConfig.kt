package com.norbertotaveras.mobilefoundation.auth.firebase

data class FirebaseAuthConfig(
    val enableAnonymousAuth: Boolean = true,
    val enableEmailPasswordAuth: Boolean = true
)