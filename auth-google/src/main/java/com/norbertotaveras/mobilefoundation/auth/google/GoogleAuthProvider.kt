package com.norbertotaveras.mobilefoundation.auth.google

import android.content.Context
import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Provider-neutral contract for Google sign-in.
 */
interface GoogleAuthProvider {
    /**
     * Starts Google sign-in for [context] using [config].
     */
    suspend fun signIn(
        context: Context,
        config: GoogleAuthConfig
    ): SdkResult<GoogleAuthToken>

    /**
     * Clears Google credential state for [context].
     */
    suspend fun signOut(context: Context): SdkResult<Unit>
}
