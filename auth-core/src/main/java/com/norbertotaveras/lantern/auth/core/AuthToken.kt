package com.norbertotaveras.lantern.auth.core

/**
 * Optional token data associated with an [AuthSession].
 */
data class AuthToken(
    val idToken: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAtEpochMillis: Long? = null
)
