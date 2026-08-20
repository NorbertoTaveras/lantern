# Authentication

Authentication is split across provider-neutral contracts and provider-specific implementations.

## Auth Core

`auth-core` defines the shared model layer:

- `AuthProvider`
- `AuthSession`
- `AuthState`
- `AuthProviderType`
- `AuthToken`
- `UserProfile`

```kotlin
val authProvider: AuthProvider = FirebaseAuthProvider()

when (val result = authProvider.signIn()) {
    is SdkResult.Success -> {
        val session = result.data
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

## Firebase Auth

Use `auth-firebase` when your app uses Firebase Authentication.

```kotlin
val firebaseAuth = FirebaseAuthProvider()

firebaseAuth.signInAnonymously()
firebaseAuth.signInWithEmailAndPassword(email, password)
firebaseAuth.createUserWithEmailAndPassword(email, password)
firebaseAuth.getCurrentSession()
firebaseAuth.observeAuthState()
firebaseAuth.signOut()
```

Firebase configuration stays in the app module. Add `google-services.json` to the consuming app, enable the Firebase providers you need, and register your app signing fingerprints in Firebase.

## Google Sign-In

Use `auth-google` when your app needs Google sign-in through Android Credential Manager.

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

Use the Web OAuth client ID as `serverClientId`. Android OAuth client IDs identify the app package and signing certificate, while the Web client ID is used to request the ID token.

## Firebase + Google

Use `auth-firebase-google` when Google sign-in should authenticate with Firebase.

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

The bridge module signs in with Google, retrieves the Google ID token, creates a Firebase credential, signs into Firebase Auth, and returns a normalized `AuthSession`.

## Sign-Out

Provider-specific sign-out can clear both SDK session state and provider credential state where supported.

For Google-backed Firebase auth, sign-out should clear Firebase auth state and Google Credential Manager state so the next sign-in can prompt account selection again when needed.
