# Google Auth

`auth-google` provides Google sign-in through Android Credential Manager.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-auth-google:$lanternVersion")
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

## Result Handling

```kotlin
when (val result = googleAuthProvider.signIn(context, googleConfig)) {
    is SdkResult.Success -> {
        val token = result.data
        sendIdTokenToBackend(token.idToken)
    }
    is SdkResult.Failure -> {
        logger.error("Google sign-in failed: ${result.error.code}")
    }
}
```

Use `filterByAuthorizedAccounts = false` when first-time users should be able to pick any Google
account on the device. Use `autoSelectEnabled = false` when your app wants account selection to be
visible during testing or explicit sign-in flows.

## Sign-Out And Credential State

```kotlin
googleAuthProvider.signOut(context)
```

Clear credential state when your app wants the next sign-in attempt to show account selection again.

## Troubleshooting

| Symptom | Check |
| --- | --- |
| No credentials available | Confirm the emulator/device has Google Play services and a Google account. |
| Sign-in is canceled after picking an account | Confirm the Web OAuth client ID and Android SHA fingerprints belong to the same Google/Firebase project. |
| Backend rejects token | Verify the backend expects the same Web OAuth client ID as the token audience. |

## Boundaries

This module does not depend on Firebase. Use `auth-firebase-google` when Google sign-in should authenticate with Firebase.
