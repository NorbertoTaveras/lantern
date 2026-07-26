package com.norbertotaveras.mobilefoundation.auth.core

data class UserProfile(
    val userId: String,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null
)