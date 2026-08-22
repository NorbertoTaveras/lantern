package com.norbertotaveras.lanternsample.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.ui.graphics.vector.ImageVector

sealed class SampleDestination(
    val route: String,
    val title: String,
    val description: String,
    val icon: ImageVector
) {
    data object Home : SampleDestination(
        route = "home",
        title = "Overview",
        description = "SDK modules and current progress",
        icon = Icons.Filled.Home
    )

    data object FirebaseAuth : SampleDestination(
        route = "firebase_auth",
        title = "Firebase Auth",
        description = "Anonymous and email/password auth",
        icon = Icons.Filled.Cloud
    )

    data object GoogleAuth : SampleDestination(
        route = "google_auth",
        title = "Google Auth",
        description = "Credential Manager sign-in",
        icon = Icons.AutoMirrored.Filled.Login
    )

    data object FirebaseGoogleAuth : SampleDestination(
        route = "firebase_google_auth",
        title = "Firebase + Google",
        description = "Google sign-in backed by Firebase",
        icon = Icons.Filled.Security
    )

    data object AuthState : SampleDestination(
        route = "auth_state",
        title = "Auth State",
        description = "Current session and provider state",
        icon = Icons.Filled.AccountCircle
    )

    data object Permissions : SampleDestination(
        route = "permissions",
        title = "Permissions",
        description = "Runtime permission resolver",
        icon = Icons.Filled.PrivacyTip
    )

    data object SecureStorage : SampleDestination(
        route = "secure_storage",
        title = "Secure Storage",
        description = "Local key-value and token storage",
        icon = Icons.Filled.Lock
    )

    data object RemoteConfig : SampleDestination(
        route = "remote_config",
        title = "Remote Config",
        description = "Firebase-backed config values",
        icon = Icons.Filled.Tune
    )

    data object FeatureFlags : SampleDestination(
        route = "feature_flags",
        title = "Feature Flags",
        description = "Typed flag evaluation",
        icon = Icons.Filled.Flag
    )

    data object Network : SampleDestination(
        route = "network",
        title = "Network",
        description = "OkHttp clients and interceptors",
        icon = Icons.Filled.SyncAlt
    )

    data object Notifications : SampleDestination(
        route = "notifications",
        title = "Notifications",
        description = "Payloads, channels, and topics",
        icon = Icons.Filled.Notifications
    )

    data object MediaPicker : SampleDestination(
        route = "media_picker",
        title = "Media Picker",
        description = "Typed Photo Picker requests",
        icon = Icons.Filled.Collections
    )

    data object Analytics : SampleDestination(
        route = "analytics",
        title = "Analytics",
        description = "Typed events and providers",
        icon = Icons.Filled.Analytics
    )

    data object DeepLinks : SampleDestination(
        route = "deep_links",
        title = "Deep Links",
        description = "URI parsing and allow-lists",
        icon = Icons.Filled.Link
    )

    data object BackgroundWork : SampleDestination(
        route = "background_work",
        title = "Background Work",
        description = "Scheduling models and status",
        icon = Icons.Filled.Work
    )

    data object AppVersioning : SampleDestination(
        route = "app_versioning",
        title = "App Versioning",
        description = "Version and update policy",
        icon = Icons.Filled.SystemUpdate
    )

    data object Logging : SampleDestination(
        route = "logging",
        title = "Logging",
        description = "SDK logger behavior",
        icon = Icons.Filled.BugReport
    )
}

val sampleDestinations = listOf(
    SampleDestination.Home,
    SampleDestination.FirebaseAuth,
    SampleDestination.GoogleAuth,
    SampleDestination.FirebaseGoogleAuth,
    SampleDestination.AuthState,
    SampleDestination.Permissions,
    SampleDestination.SecureStorage,
    SampleDestination.RemoteConfig,
    SampleDestination.FeatureFlags,
    SampleDestination.Network,
    SampleDestination.Notifications,
    SampleDestination.MediaPicker,
    SampleDestination.Analytics,
    SampleDestination.DeepLinks,
    SampleDestination.BackgroundWork,
    SampleDestination.AppVersioning,
    SampleDestination.Logging
)
