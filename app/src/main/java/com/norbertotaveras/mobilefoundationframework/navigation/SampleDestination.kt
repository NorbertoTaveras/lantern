package com.norbertotaveras.mobilefoundationframework.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Security
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
    SampleDestination.Logging
)
