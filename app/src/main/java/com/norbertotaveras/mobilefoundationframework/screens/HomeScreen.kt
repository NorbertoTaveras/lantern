package com.norbertotaveras.mobilefoundationframework.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.mobilefoundationframework.components.DemoMetric
import com.norbertotaveras.mobilefoundationframework.components.DemoSection
import com.norbertotaveras.mobilefoundationframework.components.FeatureScreen
import com.norbertotaveras.mobilefoundationframework.components.InfoRow
import com.norbertotaveras.mobilefoundationframework.components.MetricRow
import com.norbertotaveras.mobilefoundationframework.components.ModuleRow

@Composable
fun HomeScreen() {
    FeatureScreen(
        title = "Mobile Foundation SDK",
        subtitle = "A modular Android foundation library with clean SDK boundaries and a sample app for manual verification.",
        icon = Icons.Filled.Home,
        status = "FWK-37 sample"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "SDK modules", value = "12"),
                DemoMetric(label = "Live demos", value = "7"),
                DemoMetric(label = "UI modules", value = "0")
            )
        )

        DemoSection(
            title = "Current SDK modules",
            description = "The sample app showcases the modules that exist today without pulling UI into library code.",
            leadingIcon = Icons.Filled.Security
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ModuleRow(
                    name = "sdk-core",
                    description = "Shared primitives such as results, errors, config, and dispatchers.",
                    status = "Core",
                    icon = Icons.Filled.Home
                )

                ModuleRow(
                    name = "logging",
                    description = "SDK logger abstraction and Android logger implementation.",
                    status = "Ready",
                    icon = Icons.Filled.BugReport
                )

                ModuleRow(
                    name = "auth-core",
                    description = "Provider-neutral auth contracts, state, sessions, and models.",
                    status = "Ready",
                    icon = Icons.Filled.AccountCircle
                )

                ModuleRow(
                    name = "auth-firebase",
                    description = "Firebase-backed auth provider with anonymous sign-in support.",
                    status = "Live",
                    icon = Icons.Filled.Cloud
                )

                ModuleRow(
                    name = "auth-google",
                    description = "Credential Manager Google sign-in provider wired in the sample app.",
                    status = "Live",
                    icon = Icons.AutoMirrored.Filled.Login
                )

                ModuleRow(
                    name = "auth-firebase-google",
                    description = "Bridge module that authenticates Google sessions with Firebase.",
                    status = "Live",
                    icon = Icons.Filled.Security
                )

                ModuleRow(
                    name = "permissions",
                    description = "Android-version-aware runtime permission resolver.",
                    status = "Live",
                    icon = Icons.Filled.PrivacyTip
                )

                ModuleRow(
                    name = "secure-storage",
                    description = "DataStore-backed key-value storage and token persistence contracts.",
                    status = "Live",
                    icon = Icons.Filled.Lock
                )

                ModuleRow(
                    name = "network-okhttp",
                    description = "OkHttp interceptors, retry rules, and network error mapping utilities.",
                    status = "Ready",
                    icon = Icons.Filled.SyncAlt
                )

                ModuleRow(
                    name = "remote-config",
                    description = "Provider-neutral typed remote configuration contracts.",
                    status = "Ready",
                    icon = Icons.Filled.Tune
                )

                ModuleRow(
                    name = "remote-config-firebase",
                    description = "Firebase Remote Config provider implementation with defaults and fetch support.",
                    status = "Live",
                    icon = Icons.Filled.Cloud
                )

                ModuleRow(
                    name = "feature-flags",
                    description = "Typed feature flag contracts with static provider evaluation for samples and tests.",
                    status = "Live",
                    icon = Icons.Filled.Flag
                )
            }
        }

        DemoSection(
            title = "Demo status",
            description = "The screen set is intentionally scoped to the modules already created.",
            leadingIcon = Icons.Filled.Cloud
        ) {
            InfoRow(label = "Firebase anonymous sign-in", value = "Ready")
            InfoRow(label = "Google sign-in", value = "Ready")
            InfoRow(label = "Firebase + Google bridge", value = "Ready")
            InfoRow(label = "Runtime permissions", value = "Ready")
            InfoRow(label = "Secure storage", value = "Ready")
            InfoRow(label = "Remote config", value = "Ready")
            InfoRow(label = "Feature flags", value = "Ready")
        }

        DemoSection(
            title = "Current boundary",
            description = "Upcoming notification, media picker, background work, analytics, app versioning, and deep link modules are intentionally not created in this UI pass.",
            leadingIcon = Icons.Filled.BugReport
        ) {
            Text(
                text = "Use the drawer to inspect each implemented SDK area. Compose stays in the sample app, while reusable contracts and provider logic stay inside SDK modules."
            )
        }
    }
}
