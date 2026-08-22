# Auth Core

`auth-core` defines provider-neutral authentication contracts and models.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
```

## Use It For

- Writing app code against `AuthProvider` instead of Firebase or Google SDKs directly.
- Reading normalized auth sessions through `AuthSession`.
- Observing auth status with `AuthState`.
- Passing provider information with `AuthProviderType`.
- Representing user profile and token data with `UserProfile` and `AuthToken`.

## Provider Contract

```kotlin
val provider: AuthProvider = FirebaseAuthProvider()

when (val result = provider.getCurrentSession()) {
    is SdkResult.Success -> {
        val session = result.data
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

## Session State

`AuthSession` is the SDK-level view of the authenticated user. Provider implementations map vendor-specific user objects into this model so app layers can stay provider-neutral.

## Boundaries

`auth-core` should not depend on Firebase Auth, Google Credential Manager, app UI, or Compose. Provider modules implement the contracts.
