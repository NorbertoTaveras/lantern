package com.norbertotaveras.mobilefoundation.auth.core

data class AuthSession(
    val userId: String,
    val provider: AuthProviderType,
    val userProfile: UserProfile? = null,
    val authToken: AuthToken? = null
)