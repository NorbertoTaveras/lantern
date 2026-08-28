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

package com.norbertotaveras.lantern.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import com.norbertotaveras.lantern.auth.core.AuthProvider
import com.norbertotaveras.lantern.auth.core.AuthSession
import com.norbertotaveras.lantern.auth.core.AuthState
import com.norbertotaveras.lantern.auth.firebase.internal.FirebaseAuthErrorMapper
import com.norbertotaveras.lantern.auth.firebase.internal.FirebaseAuthSessionMapper
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * [AuthProvider] implementation backed by Firebase Authentication.
 */
class FirebaseAuthProvider private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionMapper: FirebaseAuthSessionMapper,
    private val errorMapper: FirebaseAuthErrorMapper
) : AuthProvider {

    /**
     * Creates a provider using [FirebaseAuth.getInstance].
     */
    constructor() : this(
        firebaseAuth = FirebaseAuth.getInstance(),
        sessionMapper = FirebaseAuthSessionMapper(),
        errorMapper = FirebaseAuthErrorMapper()
    )

    /**
     * Creates a provider using an injected [FirebaseAuth] instance.
     */
    constructor(firebaseAuth: FirebaseAuth) : this(
        firebaseAuth = firebaseAuth,
        sessionMapper = FirebaseAuthSessionMapper(),
        errorMapper = FirebaseAuthErrorMapper()
    )

    /**
     * Signs in anonymously.
     */
    override suspend fun signIn(): SdkResult<AuthSession> {
        return signInAnonymously()
    }

    /**
     * Starts Firebase anonymous sign-in and returns the normalized session.
     */
    suspend fun signInAnonymously(): SdkResult<AuthSession> {
        return try {
            val result = firebaseAuth.signInAnonymously().await()
            val user = result.user

            if (user == null) {
                SdkResult.Failure(
                    SdkError(
                        code = FirebaseAuthErrorCodes.SESSION_NOT_FOUND,
                        message = "Firebase user was not returned after anonymous sign-in."
                    )
                )
            } else {
                SdkResult.Success(sessionMapper.map(user))
            }
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }

    /**
     * Signs in with Firebase email/password authentication.
     */
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): SdkResult<AuthSession> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user == null) {
                SdkResult.Failure(
                    SdkError(
                        code = FirebaseAuthErrorCodes.SESSION_NOT_FOUND,
                        message = "Firebase user was not returned after email/password sign-in."
                    )
                )
            } else {
                SdkResult.Success(sessionMapper.map(user))
            }
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }

    /**
     * Creates a Firebase email/password account and returns the normalized session.
     */
    suspend fun createUserWithEmailAndPassword(
        email: String,
        password: String
    ): SdkResult<AuthSession> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val user = result.user

            if (user == null) {
                SdkResult.Failure(
                    SdkError(
                        code = FirebaseAuthErrorCodes.SESSION_NOT_FOUND,
                        message = "Firebase user was not returned after account creation."
                    )
                )
            } else {
                SdkResult.Success(sessionMapper.map(user))
            }
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }

    override suspend fun signOut(): SdkResult<Unit> {
        return try {
            firebaseAuth.signOut()
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }

    override suspend fun getCurrentSession(): SdkResult<AuthSession?> {
        return try {
            SdkResult.Success(firebaseAuth.currentUser?.let(sessionMapper::map))
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }

    override fun observeAuthState(): Flow<AuthState> = callbackFlow {
        val listener = FirebaseAuth.AuthStateListener { auth ->
            val state = auth.currentUser
                ?.let { AuthState.Authenticated(sessionMapper.map(it)) }
                ?: AuthState.Unauthenticated

            trySend(state)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }
}
