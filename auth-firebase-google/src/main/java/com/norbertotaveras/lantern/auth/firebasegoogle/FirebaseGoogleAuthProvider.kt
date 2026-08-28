/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.auth.firebasegoogle

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.norbertotaveras.lantern.auth.core.AuthProvider
import com.norbertotaveras.lantern.auth.core.AuthProviderType
import com.norbertotaveras.lantern.auth.core.AuthSession
import com.norbertotaveras.lantern.auth.core.AuthState
import com.norbertotaveras.lantern.auth.core.UserProfile
import com.norbertotaveras.lantern.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.lantern.auth.google.GoogleAuthConfig
import com.norbertotaveras.lantern.auth.google.GoogleAuthProvider
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.google.firebase.auth.GoogleAuthProvider as FirebaseGoogleCredentialProvider
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * [AuthProvider] that signs in with Google and exchanges the ID token with Firebase Auth.
 */
class FirebaseGoogleAuthProvider private constructor(
    private val context: Context,
    private val config: FirebaseGoogleAuthConfig,
    private val firebaseAuth: FirebaseAuth,
    private val googleAuthProvider: GoogleAuthProvider,
    @Suppress("UNUSED_PARAMETER")
    privateMarker: Unit
) : AuthProvider {

    /**
     * Creates a Firebase + Google provider using default Firebase and Credential Manager instances.
     */
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

    /**
     * Creates a Firebase + Google provider with injectable Firebase and Google dependencies.
     */
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
