# Firebase Google Auth

`auth-firebase-google` bridges Google sign-in into Firebase Authentication.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-google:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase-google:$lanternVersion")
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

## Sign-Out

```kotlin
provider.signOut()
```

Sign-out should clear Firebase auth state and Google credential state so the next sign-in flow can prompt again when needed.

## Boundaries

This module coordinates provider implementations. It should not move OAuth IDs, Firebase JSON files, or app secrets into SDK code.
