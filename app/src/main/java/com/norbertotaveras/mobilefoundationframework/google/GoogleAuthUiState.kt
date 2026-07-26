package com.norbertotaveras.mobilefoundationframework.google

data class GoogleAuthUiState(
    val isLoading: Boolean = false,
    val idTokenPreview: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val profilePictureUri: String? = null,
    val message: String? = null,
    val errorMessage: String? = null
) {
    val isSignedIn: Boolean = idTokenPreview != null
}
