package com.norbertotaveras.mobilefoundation.auth.core

/**
 * Authentication state emitted by [AuthProvider.observeAuthState].
 */
sealed interface AuthState {
    /**
     * Authentication state is being loaded.
     */
    data object Loading : AuthState
    /**
     * No authenticated session is active.
     */
    data object Unauthenticated : AuthState
    /**
     * An authenticated [session] is active.
     */
    data class Authenticated(val session: AuthSession) : AuthState
}
