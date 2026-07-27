package com.norbertotaveras.mobilefoundationframework.google

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbertotaveras.mobilefoundation.auth.google.CredentialManagerGoogleAuthProvider
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthConfig
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthProvider
import com.norbertotaveras.mobilefoundation.auth.google.GoogleAuthToken
import com.norbertotaveras.mobilefoundation.core.SdkResult
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
