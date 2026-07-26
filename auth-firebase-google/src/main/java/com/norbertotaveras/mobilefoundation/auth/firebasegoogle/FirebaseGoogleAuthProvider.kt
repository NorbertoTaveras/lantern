package com.norbertotaveras.mobilefoundation.auth.firebasegoogle

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.norbertotaveras.mobilefoundation.auth.core.AuthProvider
import com.norbertotaveras.mobilefoundation.auth.core.AuthSession
import com.norbertotaveras.mobilefoundation.auth.core.AuthState
import com.norbertotaveras.mobilefoundation.auth.firebase.FirebaseAuthErrorMapper
import com.norbertotaveras.mobilefoundation.auth.firebase.FirebaseAuthSessionMapper
import com.norbertotaveras.mobilefoundation.auth.firebase.FirebaseAuthStateObserver
import com.norbertotaveras.mobilefoundation.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthConfig
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthProvider
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.google.firebase.auth.GoogleAuthProvider as FirebaseGoogleCredentialProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await

class FirebaseGoogleAuthProvider(
    private val context: Context,
    private val config: FirebaseGoogleAuthConfig,
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val googleAuthProvider: GoogleAuthProvider = CredentialManagerGoogleAuthProvider(),
    private val sessionMapper: FirebaseAuthSessionMapper = FirebaseAuthSessionMapper(),
    private val firebaseErrorMapper: FirebaseAuthErrorMapper = FirebaseAuthErrorMapper()
) : AuthProvider {

    override suspend fun signIn(): SdkResult<AuthSession> {
        val googleResult = googleAuthProvider.signIn(
            context = context,
            config = GoogleAuthConfig(
                serverClientId = config.serverClientId,
                filterByAuthorizedAccounts = config.filterByAuthorizedAccounts,
                autoSelectEnabled = config.autoSelectEnabled
            )
        )

        val googleToken = when (googleResult) {
            is SdkResult.Success -> googleResult.data
            is SdkResult.Failure -> {
                return SdkResult.Failure(
                    googleResult.error.copy(
                        code = FirebaseGoogleAuthErrorCodes.GOOGLE_SIGN_IN_FAILED
                    )
                )
            }
        }

        return try {
            val credential = FirebaseGoogleCredentialProvider.getCredential(
                googleToken.idToken,
                null
            )

            val result = firebaseAuth.signInWithCredential(credential).await()
            val user = result.user

            if (user == null) {
                SdkResult.Failure(
                    SdkError(
                        code = FirebaseGoogleAuthErrorCodes.SESSION_NOT_FOUND,
                        message = "Firebase user was not returned after Google sign-in."
                    )
                )
            } else {
                SdkResult.Success(sessionMapper.map(user))
            }
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                firebaseErrorMapper.map(throwable).copy(
                    code = FirebaseGoogleAuthErrorCodes.FIREBASE_SIGN_IN_FAILED
                )
            )
        }
    }

    override suspend fun signOut(): SdkResult<Unit> {
        return try {
            firebaseAuth.signOut()
            googleAuthProvider.signOut(context)
        } catch (throwable: Throwable) {
            SdkResult.Failure(firebaseErrorMapper.map(throwable))
        }
    }

    override suspend fun getCurrentSession(): SdkResult<AuthSession?> {
        return try {
            SdkResult.Success(firebaseAuth.currentUser?.let(sessionMapper::map))
        } catch (throwable: Throwable) {
            SdkResult.Failure(firebaseErrorMapper.map(throwable))
        }
    }

    override fun observeAuthState(): Flow<AuthState> {
        return FirebaseAuthStateObserver(
            firebaseAuth = firebaseAuth,
            sessionMapper = sessionMapper
        ).observe()
    }
}