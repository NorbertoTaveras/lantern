package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.DestructiveDemoButton
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.SecondaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.StatusMessage
import com.norbertotaveras.mobilefoundationframework.firebase.FirebaseAuthViewModel

@Composable
fun FirebaseAuthScreen(
    viewModel: FirebaseAuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FeatureScreen(
        title = "Firebase Auth",
        subtitle = "Exercise the live Firebase Authentication provider without changing the SDK module boundary.",
        icon = Icons.Filled.Cloud,
        status = if (uiState.userId != null) "Signed in" else "Not signed in"
    ) {
        DemoSection(
            title = "Session controls",
            description = "Anonymous sign-in is the first working auth flow in the sample app.",
            leadingIcon = Icons.AutoMirrored.Filled.Login
        ) {
            if (uiState.isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            PrimaryDemoButton(
                text = "Sign in anonymously",
                icon = Icons.AutoMirrored.Filled.Login,
                enabled = !uiState.isLoading,
                onClick = viewModel::signInAnonymously
            )

            SecondaryDemoButton(
                text = "Refresh current session",
                icon = Icons.Filled.Refresh,
                enabled = !uiState.isLoading,
                onClick = viewModel::loadCurrentSession
            )

            DestructiveDemoButton(
                text = "Sign out",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                enabled = !uiState.isLoading,
                onClick = viewModel::signOut
            )
        }

        DemoSection(
            title = "Session",
            description = "This data comes from the Firebase auth provider wrapper.",
            leadingIcon = Icons.Filled.AccountCircle
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                InfoRow(
                    label = "User ID",
                    value = uiState.userId ?: "None",
                    supportingText = "Firebase UID"
                )
                InfoRow(label = "Email", value = uiState.email ?: "None")
                InfoRow(label = "Display name", value = uiState.displayName ?: "None")
                InfoRow(label = "Provider", value = uiState.provider ?: "None")
            }
        }

        StatusMessage(
            message = uiState.message,
            errorMessage = uiState.errorMessage
        )
    }
}
