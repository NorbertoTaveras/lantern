package com.norbertotaveras.mobilefoundationframework.firebasegoogle

data class FirebaseGoogleAuthUiState(
    val isLoading: Boolean = false,
    val userId: String? = null,
    val email: String? = null,
    val displayName: String? = null,
    val photoUrl: String? = null,
    val provider: String? = null,
    val message: String? = null,
    val errorMessage: String? = null
) {
    val isSignedIn: Boolean = userId != null
}
