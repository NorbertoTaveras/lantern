package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
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
import com.norbertotaveras.mobilefoundationframework.R
import com.norbertotaveras.mobilefoundationframework.components.DemoMetric
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.MetricRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.SecondaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.StatusMessage
import com.norbertotaveras.mobilefoundationframework.google.GoogleAuthViewModel

@Composable
fun GoogleAuthScreen(
    viewModel: GoogleAuthViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val serverClientId = stringResource(id = R.string.firebase_web_client_id)

    FeatureScreen(
        title = "Google Auth",
        subtitle = "Test Google Sign-In through Credential Manager and the SDK provider module.",
        icon = Icons.AutoMirrored.Filled.Login,
        status = if (uiState.isSignedIn) "Credential loaded" else "Ready"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Provider", value = "Google"),
                DemoMetric(label = "Credential", value = if (uiState.isSignedIn) "Loaded" else "Empty"),
                DemoMetric(label = "Token", value = if (uiState.idTokenPreview != null) "Preview" else "None")
            )
        )

        DemoSection(
            title = "Credential controls",
            description = "This flow requests a Google ID token through the auth-google provider only.",
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
                text = "Sign in with Google",
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
                text = "Clear credential state",
                icon = Icons.Filled.Refresh,
                enabled = !uiState.isLoading,
                onClick = {
                    viewModel.clearCredentialState(context)
                }
            )
        }

        DemoSection(
            title = "Credential result",
            description = "The ID token is intentionally shortened in the sample UI.",
            leadingIcon = Icons.Filled.AccountCircle
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Email", value = uiState.email ?: "None")
                InfoRow(label = "Display name", value = uiState.displayName ?: "None")
                InfoRow(label = "ID token", value = uiState.idTokenPreview ?: "None")
                InfoRow(label = "Profile photo", value = uiState.profilePictureUri ?: "None")
            }
        }

        DemoSection(
            title = "Implementation state",
            description = "This screen stays provider-specific; use Firebase + Google for the Firebase credential exchange.",
            leadingIcon = Icons.Filled.Security
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Provider module", value = "auth-google")
                InfoRow(label = "Runtime flow", value = "Wired")
                InfoRow(label = "Firebase bridge", value = "Separate screen")
            }
        }

        StatusMessage(
            message = uiState.message,
            errorMessage = uiState.errorMessage
        )
    }
}
