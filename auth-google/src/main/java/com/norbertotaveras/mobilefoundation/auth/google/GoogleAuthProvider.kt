package com.norbertotaveras.mobilefoundation.auth.google

import android.content.Context
import com.norbertotaveras.mobilefoundation.core.SdkResult

interface GoogleAuthProvider {
    suspend fun signIn(
        context: Context,
        config: GoogleAuthConfig
    ): SdkResult<GoogleAuthToken>

    suspend fun signOut(context: Context): SdkResult<Unit>
}