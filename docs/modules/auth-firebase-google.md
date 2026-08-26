# Firebase Google Auth

`auth-firebase-google` bridges Google sign-in into Firebase Authentication.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-auth-google:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase-google:$lanternVersion")
```

## Use It For

- Signing in with Google.
- Retrieving a Google ID token.
- Creating a Firebase credential from that token.
- Signing into Firebase Auth.
- Returning a normalized `AuthSession`.
- Clearing Firebase and Google credential state on sign-out.

## Setup

The consuming app owns both Google and Firebase configuration:

- Add `google-services.json` to the app module.
- Enable Google as a Firebase Authentication provider.
- Use the Web OAuth client ID in `FirebaseGoogleAuthConfig`.
- Register app signing fingerprints for debug and release variants.

!!! warning "Google + Firebase must match"
    Firebase Google sign-in needs both sides configured: the app's Firebase project must know the
    Android package/signing fingerprints, and `FirebaseGoogleAuthConfig.serverClientId` must use the
    Web OAuth client ID from the same Google/Firebase project.

!!! info "Configuration stays in the app"
    Keep `google-services.json`, OAuth client IDs, SHA fingerprints, and provider enablement in the
    consuming application. This SDK module coordinates sign-in; it does not own project credentials.

## Basic Usage

```kotlin
val provider = FirebaseGoogleAuthProvider(
    context = context,
    config = FirebaseGoogleAuthConfig(
        serverClientId = webOAuthClientId,
        filterByAuthorizedAccounts = false,
        autoSelectEnabled = false
    )
)

val result = provider.signIn()
```

## Result Handling

```kotlin
when (val result = provider.signIn()) {
    is SdkResult.Success -> {
        val session = result.data
        val firebaseUid = session.userId
    }
    is SdkResult.Failure -> {
        logger.error("Firebase Google sign-in failed: ${result.error.code}")
    }
}
```

The bridge returns the same provider-neutral `AuthSession` model used by `auth-core`, so app layers
do not need to depend directly on Firebase user objects.

## Sign-Out

```kotlin
when (val result = provider.signOut()) {
    is SdkResult.Success -> Unit
    is SdkResult.Failure -> logger.error("Sign-out failed: ${result.error.code}")
}
```

Sign-out clears Firebase auth state and Google credential state so the next sign-in flow can prompt
again when needed.

## Recommended App Flow

```kotlin
val existingSession = provider.getCurrentSession()

if (existingSession is SdkResult.Success && existingSession.data != null) {
    showSignedInUi(existingSession.data)
} else {
    showGoogleSignInButton()
}
```

Use `authState` from the provider when the UI should react to Firebase auth changes over time.

## Boundaries

This module coordinates provider implementations. It should not move OAuth IDs, Firebase JSON files, or app secrets into SDK code.
