package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.norbertotaveras.mobilefoundationframework.firebase.FirebaseAuthUiState
import com.norbertotaveras.mobilefoundationframework.firebase.FirebaseAuthViewModel

@Composable
fun FirebaseAuthScreen(
    viewModel: FirebaseAuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FirebaseAuthContent(
        uiState = uiState,
        onAnonymousSignInClick = viewModel::signInAnonymously,
        onSignOutClick = viewModel::signOut,
        onRefreshSessionClick = viewModel::loadCurrentSession
    )
}

@Composable
private fun FirebaseAuthContent(
    uiState: FirebaseAuthUiState,
    onAnonymousSignInClick: () -> Unit,
    onSignOutClick: () -> Unit,
    onRefreshSessionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Firebase Auth")
        Text(text = "Test anonymous Firebase Authentication through the SDK.")

        if (uiState.isLoading) {
            CircularProgressIndicator()
        }

        Button(
            onClick = onAnonymousSignInClick,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in anonymously")
        }

        OutlinedButton(
            onClick = onSignOutClick,
            enabled = !uiState.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign out")
        }

        TextButton(
            onClick = onRefreshSessionClick,
            enabled = !uiState.isLoading
        ) {
            Text(text = "Refresh current session")
        }

        FirebaseAuthStateCard(uiState = uiState)
    }
}

@Composable
private fun FirebaseAuthStateCard(
    uiState: FirebaseAuthUiState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Session")

            Text(text = "User ID: ${uiState.userId ?: "None"}")
            Text(text = "Email: ${uiState.email ?: "None"}")
            Text(text = "Display name: ${uiState.displayName ?: "None"}")
            Text(text = "Provider: ${uiState.provider ?: "None"}")

            uiState.message?.let { message ->
                Text(text = "Message: $message")
            }

            uiState.errorMessage?.let { error ->
                Text(text = "Error: $error")
            }
        }
    }
}