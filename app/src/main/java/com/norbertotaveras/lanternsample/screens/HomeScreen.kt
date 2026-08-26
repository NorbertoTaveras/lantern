package com.norbertotaveras.lanternsample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.SyncAlt
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lanternsample.BuildConfig
import com.norbertotaveras.lanternsample.R
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.ModuleRow

@Composable
fun HomeScreen() {
    FeatureScreen(
        title = "Lantern",
        subtitle = "A modular Android foundation library with clean SDK boundaries and a sample app for manual verification.",
        icon = Icons.Filled.Home,
        status = "v${BuildConfig.LANTERN_VERSION}",
        logoResId = R.drawable.lantern_logo
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "SDK modules", value = "21"),
                DemoMetric(label = "Live demos", value = "17"),
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

                ModuleRow(
                    name = "notifications",
                    description = "Payload parsing, notification channels, permission state, and topic models.",
                    status = "Live",
                    icon = Icons.Filled.Notifications
                )

                ModuleRow(
                    name = "notifications-firebase",
                    description = "Firebase Messaging token and topic provider helpers.",
                    status = "Ready",
                    icon = Icons.Filled.Cloud
                )

                ModuleRow(
                    name = "media-picker",
                    description = "Typed Android Photo Picker requests and result models.",
                    status = "Live",
                    icon = Icons.Filled.Collections
                )

                ModuleRow(
                    name = "analytics",
                    description = "Provider-neutral event, value, user ID, and user property models.",
                    status = "Live",
                    icon = Icons.Filled.Analytics
                )

                ModuleRow(
                    name = "analytics-firebase",
                    description = "Firebase Analytics provider implementation for typed SDK events.",
                    status = "Live",
                    icon = Icons.Filled.Cloud
                )

                ModuleRow(
                    name = "deep-links",
                    description = "URI parsing with scheme and host allow-listing.",
                    status = "Live",
                    icon = Icons.Filled.Link
                )

                ModuleRow(
                    name = "background-work",
                    description = "Work scheduling contracts, no-op scheduler, and WorkManager adapter.",
                    status = "Live",
                    icon = Icons.Filled.Work
                )

                ModuleRow(
                    name = "app-versioning",
                    description = "Installed version lookup and update policy evaluation.",
                    status = "Live",
                    icon = Icons.Filled.SystemUpdate
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
            InfoRow(label = "Network", value = "Ready")
            InfoRow(label = "Notifications", value = "Ready")
            InfoRow(label = "Media picker", value = "Ready")
            InfoRow(label = "Analytics", value = "Ready")
            InfoRow(label = "Deep links", value = "Ready")
            InfoRow(label = "Background work", value = "Ready")
            InfoRow(label = "App versioning", value = "Ready")
        }

        DemoSection(
            title = "Current boundary",
            description = "The sample app now covers each published SDK module while keeping Compose UI and provider configuration outside library modules.",
            leadingIcon = Icons.Filled.BugReport
        ) {
            Text(
                text = "Use the drawer to inspect each SDK area. Compose stays in the sample app, while reusable contracts and provider logic stay inside SDK modules."
            )
        }
    }
}
