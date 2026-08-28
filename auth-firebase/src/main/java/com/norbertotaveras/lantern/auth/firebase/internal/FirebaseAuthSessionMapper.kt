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
