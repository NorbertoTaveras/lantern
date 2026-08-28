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

package com.norbertotaveras.lantern.auth.google

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.norbertotaveras.lantern.auth.google.internal.GoogleAuthErrorMapper
import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

/**
 * [GoogleAuthProvider] implementation backed by Android Credential Manager.
 */
class CredentialManagerGoogleAuthProvider private constructor(
    private val errorMapper: GoogleAuthErrorMapper = GoogleAuthErrorMapper()
) : GoogleAuthProvider {

    /**
     * Creates a Credential Manager Google auth provider.
     */
    constructor() : this(GoogleAuthErrorMapper())

    /**
     * Launches Google sign-in and returns a Google ID token credential.
     */
    override suspend fun signIn(
        context: Context,
        config: GoogleAuthConfig
    ): SdkResult<GoogleAuthToken> {
        return try {
            val credentialManager = CredentialManager.create(context)

            val credentialOption = if (config.filterByAuthorizedAccounts) {
                GetGoogleIdOption.Builder()
                    .setServerClientId(config.serverClientId)
                    .setFilterByAuthorizedAccounts(true)
                    .setAutoSelectEnabled(config.autoSelectEnabled)
                    .build()
            } else {
                GetSignInWithGoogleOption.Builder(
                    serverClientId = config.serverClientId
                ).build()
            }

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(credentialOption)
                .build()

            val result = credentialManager.getCredential(
                context = context,
                request = request
            )

            val credential = GoogleIdTokenCredential.createFrom(result.credential.data)

            SdkResult.Success(
                GoogleAuthToken(
                    idToken = credential.idToken,
                    displayName = credential.displayName,
                    email = credential.id,
                    profilePictureUri = credential.profilePictureUri?.toString()
                )
            )
        } catch (throwable: GoogleIdTokenParsingException) {
            SdkResult.Failure(
                SdkError(
                    code = GoogleAuthErrorCodes.INVALID_CREDENTIAL,
                    message = "Unable to parse Google ID token credential.",
                    cause = throwable
                )
            )
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }

    /**
     * Clears Credential Manager credential state for sign-out.
     */
    override suspend fun signOut(context: Context): SdkResult<Unit> {
        return try {
            val credentialManager = CredentialManager.create(context)

            credentialManager.clearCredentialState(
                ClearCredentialStateRequest()
            )

            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(errorMapper.map(throwable))
        }
    }
}
