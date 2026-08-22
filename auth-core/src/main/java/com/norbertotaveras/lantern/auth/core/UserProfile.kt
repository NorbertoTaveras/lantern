package com.norbertotaveras.lantern.auth.core

/**
 * Provider-neutral user profile associated with an auth session.
 */
data class UserProfile(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null
)
