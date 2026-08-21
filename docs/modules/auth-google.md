# Google Auth

`auth-google` provides Google sign-in through Android Credential Manager.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-google:$mobileFoundationVersion")
```

## Use It For

- Launching Google sign-in through Credential Manager.
- Retrieving a Google ID token.
- Clearing provider credential state.
- Mapping Google and Credential Manager failures into SDK errors.

## Setup

The consuming app owns Google OAuth setup:

- Create Android OAuth clients for each app signing certificate.
- Use the Web OAuth client ID as `serverClientId`.
- Add debug and release SHA fingerprints in Firebase or Google Cloud as needed.
- Test on an emulator or device with Google Play services.

!!! warning "Use the Web OAuth client ID"
    `serverClientId` should be the Web OAuth client ID. Android OAuth clients identify
    package names and signing certificates; the Web client ID is what requests the Google ID token.

!!! tip "Emulator and account setup"
    Google sign-in requires Google Play services and an available Google account on the device or
    emulator. If Credential Manager cannot find credentials, add an account in Android Settings or
    sign in through a Google app on the emulator.

## Basic Usage

```kotlin
val googleAuthProvider = CredentialManagerGoogleAuthProvider()

val result = googleAuthProvider.signIn(
    context = context,
    config = GoogleAuthConfig(
        serverClientId = webOAuthClientId,
        filterByAuthorizedAccounts = false,
        autoSelectEnabled = false
    )
)
```

The returned token can be verified by your backend or exchanged by another provider module.

## Sign-Out And Credential State

```kotlin
googleAuthProvider.signOut(context)
```

Clear credential state when your app wants the next sign-in attempt to show account selection again.

## Boundaries

This module does not depend on Firebase. Use `auth-firebase-google` when Google sign-in should authenticate with Firebase.
