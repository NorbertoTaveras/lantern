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

package com.norbertotaveras.lanternsample.firebasegoogle

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbertotaveras.lantern.auth.core.AuthSession
import com.norbertotaveras.lantern.auth.firebasegoogle.FirebaseGoogleAuthConfig
import com.norbertotaveras.lantern.auth.firebasegoogle.FirebaseGoogleAuthProvider
import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FirebaseGoogleAuthViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FirebaseGoogleAuthUiState())
    val uiState: StateFlow<FirebaseGoogleAuthUiState> = _uiState.asStateFlow()

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

            when (
                val result = createProvider(
                    context = context,
                    serverClientId = trimmedClientId
                ).signIn()
            ) {
                is SdkResult.Success -> {
                    _uiState.value = result.data.toUiState(
                        message = "Signed in with Firebase + Google."
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

    fun signOut(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            when (val result = createProvider(context = context).signOut()) {
                is SdkResult.Success -> {
                    _uiState.value = FirebaseGoogleAuthUiState(
                        message = "Signed out of Firebase + Google."
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

    fun loadCurrentSession(context: Context) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            when (val result = createProvider(context = context).getCurrentSession()) {
                is SdkResult.Success -> {
                    val session = result.data

                    _uiState.value = if (session == null) {
                        FirebaseGoogleAuthUiState(
                            message = "No Firebase + Google session found."
                        )
                    } else {
                        session.toUiState(
                            message = "Current Firebase session loaded."
                        )
                    }
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

    private fun createProvider(
        context: Context,
        serverClientId: String = ""
    ): FirebaseGoogleAuthProvider {
        return FirebaseGoogleAuthProvider(
            context = context.applicationContext,
            config = FirebaseGoogleAuthConfig(
                serverClientId = serverClientId,
                filterByAuthorizedAccounts = false,
                autoSelectEnabled = false
            )
        )
    }

    private fun AuthSession.toUiState(message: String): FirebaseGoogleAuthUiState {
        return FirebaseGoogleAuthUiState(
            isLoading = false,
            userId = userId,
            email = userProfile?.email,
            displayName = userProfile?.displayName,
            photoUrl = userProfile?.photoUrl,
            provider = provider.name,
            message = message,
            errorMessage = null
        )
    }
}
