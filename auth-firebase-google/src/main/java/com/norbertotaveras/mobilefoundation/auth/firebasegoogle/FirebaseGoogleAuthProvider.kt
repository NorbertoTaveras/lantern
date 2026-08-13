package com.norbertotaveras.mobilefoundation.auth.firebasegoogle

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.norbertotaveras.mobilefoundation.auth.core.AuthProvider
import com.norbertotaveras.mobilefoundation.auth.core.AuthProviderType
import com.norbertotaveras.mobilefoundation.auth.core.AuthSession
import com.norbertotaveras.mobilefoundation.auth.core.AuthState
import com.norbertotaveras.mobilefoundation.auth.core.UserProfile
import com.norbertotaveras.mobilefoundation.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthConfig
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthProvider
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.google.firebase.auth.GoogleAuthProvider as FirebaseGoogleCredentialProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseGoogleAuthProvider private constructor(
    private val context: Context,
    private val config: FirebaseGoogleAuthConfig,
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthProvider: GoogleAuthProvider,
    @Suppress("UNUSED_PARAMETER")
    privateMarker: Unit
) : AuthProvider {

    constructor(
        context: Context,
        config: FirebaseGoogleAuthConfig
    ) : this(
        context = context,
        config = config,
        firebaseAuth = FirebaseAuth.getInstance(),
        googleAuthProvider = CredentialManagerGoogleAuthProvider(),
        privateMarker = Unit
    )

    constructor(
        context: Context,
        config: FirebaseGoogleAuthConfig,
        firebaseAuth: FirebaseAuth,
        googleAuthProvider: GoogleAuthProvider
    ) : this(
        context = context,
        config = config,
        firebaseAuth = firebaseAuth,
        googleAuthProvider = googleAuthProvider,
        privateMarker = Unit
    )

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
                SdkResult.Success(
                    AuthSession(
                        userId = user.uid,
                        provider = AuthProviderType.FirebaseGoogle,
                        userProfile = UserProfile(
                            userId = user.uid,
                            email = user.email,
                            displayName = user.displayName,
                            photoUrl = user.photoUrl?.toString()
                        )
                    )
                )
            }
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = FirebaseGoogleAuthErrorCodes.FIREBASE_SIGN_IN_FAILED,
                    message = throwable.localizedMessage ?: "Firebase Google sign-in failed.",
                    cause = throwable
                )
            )
        }
    }

    override suspend fun signOut(): SdkResult<Unit> {
        return try {
            firebaseAuth.signOut()
            googleAuthProvider.signOut(context)
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = FirebaseGoogleAuthErrorCodes.SIGN_OUT_FAILED,
                    message = throwable.localizedMessage ?: "Firebase Google sign-out failed.",
                    cause = throwable
                )
            )
        }
    }

    override suspend fun getCurrentSession(): SdkResult<AuthSession?> {
        return try {
            SdkResult.Success(
                firebaseAuth.currentUser?.let { user ->
                    AuthSession(
                        userId = user.uid,
                        provider = AuthProviderType.FirebaseGoogle,
                        userProfile = UserProfile(
                            userId = user.uid,
                            email = user.email,
                            displayName = user.displayName,
                            photoUrl = user.photoUrl?.toString()
                        )
                    )
                }
            )
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = FirebaseGoogleAuthErrorCodes.SESSION_LOOKUP_FAILED,
                    message = throwable.localizedMessage ?: "Firebase Google session lookup failed.",
                    cause = throwable
                )
            )
        }
    }

    override fun observeAuthState(): Flow<AuthState> = callbackFlow {
        val authProvider = firebaseAuth
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val state = auth.currentUser
                ?.let { user ->
                    AuthState.Authenticated(
                        AuthSession(
                            userId = user.uid,
                            provider = AuthProviderType.FirebaseGoogle,
                            userProfile = UserProfile(
                                userId = user.uid,
                                email = user.email,
                                displayName = user.displayName,
                                photoUrl = user.photoUrl?.toString()
                            )
                        )
                    )
                }
                ?: AuthState.Unauthenticated

            trySend(state)
        }

        authProvider.addAuthStateListener(listener)

        awaitClose {
            authProvider.removeAuthStateListener(listener)
        }
    }
}
