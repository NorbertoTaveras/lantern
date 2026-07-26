package com.norbertotaveras.mobilefoundationframework.firebase

data class FirebaseAuthUiState(
    val isLoading: Boolean = false,
    val userId: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val provider: String? = null,
    val message: String? = null,
    val errorMessage: String? = null
)