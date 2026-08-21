# Firebase Auth

`auth-firebase` implements Mobile Foundation auth contracts with Firebase Authentication.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase:$mobileFoundationVersion")
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
