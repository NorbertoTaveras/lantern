package com.norbertotaveras.mobilefoundation.auth.core

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

interface AuthProvider {
    suspend fun signIn(): SdkResult<AuthSession>
    suspend fun signOut(): SdkResult<Unit>
    suspend fun getCurrentSession(): SdkResult<AuthSession?>
    fun observeAuthState(): Flow<AuthState>
}