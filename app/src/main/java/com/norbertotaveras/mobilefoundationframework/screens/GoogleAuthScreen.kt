package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton
import com.norbertotaveras.mobilefoundationframework.components.SecondaryDemoButton

@Composable
fun GoogleAuthScreen() {
    FeatureScreen(
        title = "Google Auth",
        subtitle = "Credential Manager sign-in UI is prepared for a later implementation slice.",
        icon = Icons.AutoMirrored.Filled.Login,
        status = "UI prepared"
    ) {
        DemoSection(
            title = "Prepared actions",
            description = "Controls are shown for the expected workflow but intentionally disabled for now.",
            leadingIcon = Icons.AutoMirrored.Filled.Login
        ) {
            PrimaryDemoButton(
                text = "Sign in with Google",
                icon = Icons.AutoMirrored.Filled.Login,
                enabled = false,
                onClick = {}
            )

            SecondaryDemoButton(
                text = "Clear credential state",
                icon = Icons.Filled.Refresh,
                enabled = false,
                onClick = {}
            )
        }

        DemoSection(
            title = "Implementation state",
            description = "The SDK module exists, but this commit only polishes the demo shell.",
            leadingIcon = Icons.Filled.Security
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Provider module", value = "auth-google")
                InfoRow(label = "Runtime flow", value = "Not wired")
                InfoRow(label = "Credential Manager", value = "Planned")
            }
        }
    }
}
