package com.norbertotaveras.mobilefoundationframework.navigation

sealed class SampleDestination(
    val route: String,
    val title: String,
    val description: String
) {
    data object Home : SampleDestination(
        route = "home",
        title = "Home",
        description = "SDK sample app overview"
    )

    data object FirebaseAuth : SampleDestination(
        route = "firebase_auth",
        title = "Firebase Auth",
        description = "Email/password and anonymous Firebase authentication"
    )

    data object GoogleAuth : SampleDestination(
        route = "google_auth",
        title = "Google Auth",
        description = "Google Sign-In using Credential Manager"
    )

    data object FirebaseGoogleAuth : SampleDestination(
        route = "firebase_google_auth",
        title = "Firebase Google Auth",
        description = "Google Sign-In connected to Firebase Authentication"
    )

    data object AuthState : SampleDestination(
        route = "auth_state",
        title = "Auth State",
        description = "Current session and authentication state"
    )

    data object Logging : SampleDestination(
        route = "logging",
        title = "Logging",
        description = "SDK logger demo"
    )
}

val sampleDestinations = listOf(
    SampleDestination.Home,
    SampleDestination.FirebaseAuth,
    SampleDestination.GoogleAuth,
    SampleDestination.FirebaseGoogleAuth,
    SampleDestination.AuthState,
    SampleDestination.Logging
)