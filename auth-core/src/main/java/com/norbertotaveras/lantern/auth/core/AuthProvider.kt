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

package com.norbertotaveras.lantern.auth.core

import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.flow.Flow

/**
 * Provider-neutral authentication contract.
 */
interface AuthProvider {
    /**
     * Starts the provider's default sign-in flow.
     */
    suspend fun signIn(): SdkResult<AuthSession>
    /**
     * Signs the current user out of the provider.
     */
    suspend fun signOut(): SdkResult<Unit>
    /**
     * Returns the current session, or `null` when no user is signed in.
     */
    suspend fun getCurrentSession(): SdkResult<AuthSession?>
    /**
     * Observes authentication state changes.
     */
    fun observeAuthState(): Flow<AuthState>
}
