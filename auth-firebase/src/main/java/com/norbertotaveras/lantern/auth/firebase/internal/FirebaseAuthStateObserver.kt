package com.norbertotaveras.lantern.auth.firebase.internal

import com.google.firebase.auth.FirebaseAuth
import com.norbertotaveras.lantern.auth.core.AuthState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

internal class FirebaseAuthStateObserver(
    private val firebaseAuth: FirebaseAuth,
    private val sessionMapper: FirebaseAuthSessionMapper
) {

    fun observe(): Flow<AuthState> = callbackFlow {
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
