package com.norbertotaveras.lantern.auth.core

/**
 * Normalized authenticated session.
 */
data class AuthSession(
    val userId: String,
    val provider: AuthProviderType,
    val userProfile: UserProfile? = null,
    val authToken: AuthToken? = null
)
