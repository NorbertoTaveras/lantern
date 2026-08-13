package com.norbertotaveras.mobilefoundation.auth.firebase

import com.google.firebase.auth.FirebaseAuth
import com.norbertotaveras.mobilefoundation.auth.core.AuthProvider
import com.norbertotaveras.mobilefoundation.auth.core.AuthSession
import com.norbertotaveras.mobilefoundation.auth.core.AuthState
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseAuthProvider private constructor(
    private val firebaseAuth: FirebaseAuth,
    private val sessionMapper: FirebaseAuthSessionMapper,
    private val errorMapper: FirebaseAuthErrorMapper
) : AuthProvider {

    constructor() : this(
        firebaseAuth = FirebaseAuth.getInstance(),
        sessionMapper = FirebaseAuthSessionMapper(),
        errorMapper = FirebaseAuthErrorMapper()
    )

    constructor(firebaseAuth: FirebaseAuth) : this(
        firebaseAuth = firebaseAuth,
        sessionMapper = FirebaseAuthSessionMapper(),
        errorMapper = FirebaseAuthErrorMapper()
    )

    override suspend fun signIn(): SdkResult<AuthSession> {
        return signInAnonymously()
    }

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
