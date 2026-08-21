# Firebase Google Auth

`auth-firebase-google` bridges Google sign-in into Firebase Authentication.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-google:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase-google:$mobileFoundationVersion")
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
