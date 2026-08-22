# Firebase Auth

`auth-firebase` implements Lantern auth contracts with Firebase Authentication.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase:$lanternVersion")
```

## Use It For

- Anonymous sign-in.
- Email/password sign-in.
- Email/password account creation.
- Sign-out.
- Current session lookup.
- Firebase auth state observation.
- Firebase error mapping.

## Setup

Firebase configuration belongs to the consuming app:

- Add `google-services.json` to the app module.
- Apply the Google Services plugin in the app.
- Enable the Firebase Authentication providers your app uses.
- Register debug and release signing fingerprints where required.

!!! info "App-owned Firebase setup"
    The SDK module does not ship Firebase project files or secrets. Keep `google-services.json`,
    Firebase app registration, package names, and signing fingerprints in the consuming app.

!!! tip "Authentication provider setup"
    Anonymous sign-in requires the Anonymous provider to be enabled in Firebase Authentication.
    Email/password flows require the Email/Password provider to be enabled.

## Basic Usage

```kotlin
val firebaseAuth = FirebaseAuthProvider()

val signInResult = firebaseAuth.signInAnonymously()
val currentSession = firebaseAuth.getCurrentSession()
val authStates = firebaseAuth.observeAuthState()
```

Email/password flows are available from the same provider:

```kotlin
firebaseAuth.signInWithEmailAndPassword(email, password)
firebaseAuth.createUserWithEmailAndPassword(email, password)
```

## Sign-Out

```kotlin
firebaseAuth.signOut()
```

Sign-out clears Firebase Auth state for the current Firebase app instance.

## Boundaries

This module may depend on Firebase Auth, but it must not own `google-services.json` or app-specific Firebase project configuration.
