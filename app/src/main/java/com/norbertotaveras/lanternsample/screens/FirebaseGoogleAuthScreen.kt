package com.norbertotaveras.lanternsample.screens

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
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.norbertotaveras.lanternsample.R
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.DestructiveDemoButton
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import com.norbertotaveras.lanternsample.firebasegoogle.FirebaseGoogleAuthViewModel

@Composable
fun FirebaseGoogleAuthScreen(
    viewModel: FirebaseGoogleAuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val serverClientId = stringResource(id = R.string.firebase_web_client_id)

    FeatureScreen(
        title = "Firebase + Google",
        subtitle = "Sign in with Google, exchange the ID token with Firebase Auth, and read the normalized SDK session.",
        icon = Icons.Filled.Security,
        status = if (uiState.isSignedIn) "Signed in" else "Ready"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Google", value = "Token"),
                DemoMetric(label = "Firebase", value = if (uiState.isSignedIn) "Session" else "Ready"),
                DemoMetric(label = "Provider", value = uiState.provider ?: "None")
            )
        )

        DemoSection(
            title = "Bridge controls",
            description = "This flow invokes auth-firebase-google and clears both Firebase and Google credential state on sign-out.",
            leadingIcon = Icons.Filled.Security
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
                text = "Sign in with Firebase + Google",
                icon = Icons.AutoMirrored.Filled.Login,
                enabled = !uiState.isLoading,
                onClick = {
                    viewModel.signIn(
                        context = context,
                        serverClientId = serverClientId
                    )
                }
            )

            SecondaryDemoButton(
                text = "Refresh current session",
                icon = Icons.Filled.Refresh,
                enabled = !uiState.isLoading,
                onClick = {
                    viewModel.loadCurrentSession(context)
                }
            )

            DestructiveDemoButton(
                text = "Sign out",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                enabled = !uiState.isLoading,
                onClick = {
                    viewModel.signOut(context)
                }
            )
        }

        DemoSection(
            title = "Firebase session",
            description = "This session is returned after Firebase accepts the Google ID token.",
            leadingIcon = Icons.Filled.AccountCircle
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(
                    label = "User ID",
                    value = uiState.userId ?: "None",
                    supportingText = "Firebase UID"
                )
                InfoRow(label = "Email", value = uiState.email ?: "None")
                InfoRow(label = "Display name", value = uiState.displayName ?: "None")
                InfoRow(label = "Photo URL", value = uiState.photoUrl ?: "None")
                InfoRow(label = "Provider", value = uiState.provider ?: "None")
            }
        }

        DemoSection(
            title = "Bridge responsibilities",
            description = "The module composes Google Credential Manager with Firebase Auth.",
            leadingIcon = Icons.Filled.Cloud
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Google credential", value = "Live")
                InfoRow(label = "Firebase credential exchange", value = "Live")
                InfoRow(label = "Runtime flow", value = "Wired")
            }
        }

        StatusMessage(
            message = uiState.message,
            errorMessage = uiState.errorMessage
        )
    }
}
