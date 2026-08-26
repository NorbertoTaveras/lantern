package com.norbertotaveras.lantern.auth.firebase.internal

import com.google.firebase.auth.FirebaseUser
import com.norbertotaveras.lantern.auth.core.AuthProviderType
import com.norbertotaveras.lantern.auth.core.AuthSession
import com.norbertotaveras.lantern.auth.core.UserProfile

internal class FirebaseAuthSessionMapper {

    fun map(user: FirebaseUser): AuthSession {
        return AuthSession(
            userId = user.uid,
            provider = resolveProvider(user),
            userProfile = UserProfile(
                userId = user.uid,
                email = user.email,
                displayName = user.displayName,
                photoUrl = user.photoUrl?.toString()
            )
        )
    }

    private fun resolveProvider(user: FirebaseUser): AuthProviderType {
        return when {
            user.isAnonymous -> AuthProviderType.Anonymous
            user.providerData.any { it.providerId == GOOGLE_PROVIDER_ID } -> {
                AuthProviderType.FirebaseGoogle
            }
            user.email != null -> AuthProviderType.EmailPassword
            else -> AuthProviderType.Firebase
        }
    }

    private companion object {
        const val GOOGLE_PROVIDER_ID = "google.com"
    }
}
