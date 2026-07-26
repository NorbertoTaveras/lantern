package com.norbertotaveras.mobilefoundation.auth.core

data class AuthToken(
    val idToken: String? = null,
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val expiresAtEpochMillis: Long? = null
)