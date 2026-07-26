package com.norbertotaveras.mobilefoundationframework.firebase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.norbertotaveras.mobilefoundation.auth.core.AuthSession
import com.norbertotaveras.mobilefoundation.auth.firebase.FirebaseAuthProvider
import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FirebaseAuthViewModel(
    private val authProvider: FirebaseAuthProvider = FirebaseAuthProvider()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FirebaseAuthUiState())
    val uiState: StateFlow<FirebaseAuthUiState> = _uiState.asStateFlow()

    init {
        loadCurrentSession()
    }

    fun signInAnonymously() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            when (val result = authProvider.signInAnonymously()) {
                is SdkResult.Success -> {
                    _uiState.value = result.data.toUiState(
                        message = "Signed in anonymously."
                    )
                }

                is SdkResult.Failure -> {
                    _uiState.value = FirebaseAuthUiState(
                        isLoading = false,
                        errorMessage = result.error.message
                    )
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            when (val result = authProvider.signOut()) {
                is SdkResult.Success -> {
                    _uiState.value = FirebaseAuthUiState(
                        message = "Signed out."
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

    fun loadCurrentSession() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null,
                errorMessage = null
            )

            when (val result = authProvider.getCurrentSession()) {
                is SdkResult.Success -> {
                    val session = result.data

                    _uiState.value = if (session == null) {
                        FirebaseAuthUiState(
                            message = "No Firebase session found."
                        )
                    } else {
                        session.toUiState(
                            message = "Current Firebase session loaded."
                        )
                    }
                }

                is SdkResult.Failure -> {
                    _uiState.value = FirebaseAuthUiState(
                        isLoading = false,
                        errorMessage = result.error.message
                    )
                }
            }
        }
    }

    private fun AuthSession.toUiState(message: String): FirebaseAuthUiState {
        return FirebaseAuthUiState(
            isLoading = false,
            userId = userId,
            email = userProfile?.email,
            displayName = userProfile?.displayName,
            provider = provider.name,
            message = message,
            errorMessage = null
        )
    }
}