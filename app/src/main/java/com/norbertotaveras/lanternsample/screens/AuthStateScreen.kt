package com.norbertotaveras.lanternsample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Security
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow

@Composable
fun AuthStateScreen() {
    FeatureScreen(
        title = "Auth State",
        subtitle = "A simple readout surface for provider-neutral session state.",
        icon = Icons.Filled.AccountCircle,
        status = "Simple"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Contract", value = "auth-core"),
                DemoMetric(label = "Sessions", value = "Unified"),
                DemoMetric(label = "Providers", value = "3")
            )
        )

        DemoSection(
            title = "State model",
            description = "The app currently demonstrates Firebase session state on the Firebase Auth screen.",
            leadingIcon = Icons.Filled.Security
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Contract module", value = "auth-core")
                InfoRow(label = "Session model", value = "AuthSession")
                InfoRow(label = "Provider state", value = "Available to SDK modules")
                InfoRow(label = "Firebase readout", value = "Firebase screen")
                InfoRow(label = "Google credential readout", value = "Google screen")
            }
        }
    }
}
