package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.DestructiveDemoButton
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.PrimaryDemoButton

@Composable
fun FirebaseGoogleAuthScreen() {
    FeatureScreen(
        title = "Firebase + Google",
        subtitle = "Bridge screen prepared for Google sign-in backed by Firebase Auth.",
        icon = Icons.Filled.Security,
        status = "UI prepared"
    ) {
        DemoSection(
            title = "Prepared bridge flow",
            description = "The visible controls match the planned workflow but do not invoke auth behavior yet.",
            leadingIcon = Icons.Filled.Security
        ) {
            PrimaryDemoButton(
                text = "Sign in with Firebase + Google",
                icon = Icons.AutoMirrored.Filled.Login,
                enabled = false,
                onClick = {}
            )

            DestructiveDemoButton(
                text = "Sign out",
                icon = Icons.Filled.Security,
                enabled = false,
                onClick = {}
            )
        }

        DemoSection(
            title = "Bridge responsibilities",
            description = "This prepared view reflects the auth-firebase-google module contract.",
            leadingIcon = Icons.Filled.Cloud
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Google credential", value = "Planned")
                InfoRow(label = "Firebase credential exchange", value = "Planned")
                InfoRow(label = "Runtime flow", value = "Not wired")
            }
        }
    }
}
