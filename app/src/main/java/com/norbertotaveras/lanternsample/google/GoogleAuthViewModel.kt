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

package com.norbertotaveras.lanternsample.google

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbertotaveras.lantern.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.lantern.auth.google.GoogleAuthConfig
import com.norbertotaveras.lantern.auth.google.GoogleAuthProvider
import com.norbertotaveras.lantern.auth.google.GoogleAuthToken
import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoogleAuthViewModel(
    private val authProvider: GoogleAuthProvider = CredentialManagerGoogleAuthProvider()
) : ViewModel() {

    private val _uiState = MutableStateFlow(GoogleAuthUiState())
    val uiState: StateFlow<GoogleAuthUiState> = _uiState.asStateFlow()

    fun signIn(
        context: Context,
        serverClientId: String
    ) {
        val trimmedClientId = serverClientId.trim()

        if (trimmedClientId.isBlank()) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                message = null,
                errorMessage = "Missing FIREBASE_WEB_CLIENT_ID in local.properties."
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            val config = GoogleAuthConfig(
                serverClientId = trimmedClientId,
                filterByAuthorizedAccounts = false,
                autoSelectEnabled = false
            )

            when (val result = authProvider.signIn(context, config)) {
                is SdkResult.Success -> {
                    _uiState.value = result.data.toUiState(
                        message = "Google credential loaded."
                    )
                }

                is SdkResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.error.message
                    )
                }
            }
        }
    }

    fun clearCredentialState(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            when (val result = authProvider.signOut(context)) {
                is SdkResult.Success -> {
                    _uiState.value = GoogleAuthUiState(
                        message = "Google credential state cleared."
                    )
                }

                is SdkResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = result.error.message
                    )
                }
            }
        }
    }

    private fun GoogleAuthToken.toUiState(message: String): GoogleAuthUiState {
        return GoogleAuthUiState(
            isLoading = false,
            idTokenPreview = idToken.toPreview(),
            email = email,
            displayName = displayName,
            profilePictureUri = profilePictureUri,
            message = message,
            errorMessage = null
        )
    }

    private fun String.toPreview(): String {
        return if (length <= TokenPreviewLength) {
            this
        } else {
            "${take(TokenPreviewLength)}..."
        }
    }

    private companion object {
        const val TokenPreviewLength = 12
    }
}
