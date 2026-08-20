package com.norbertotaveras.mobilefoundation.auth.core

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

/**
 * Provider-neutral authentication contract.
 */
interface AuthProvider {
    /**
     * Starts the provider's default sign-in flow.
     */
    suspend fun signIn(): SdkResult<AuthSession>
    /**
     * Signs the current user out of the provider.
     */
    suspend fun signOut(): SdkResult<Unit>
    /**
     * Returns the current session, or `null` when no user is signed in.
     */
    suspend fun getCurrentSession(): SdkResult<AuthSession?>
    /**
     * Observes authentication state changes.
     */
    fun observeAuthState(): Flow<AuthState>
}
