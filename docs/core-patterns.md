# Core Patterns

## Result Handling

Lantern uses `SdkResult<T>` for expected SDK outcomes.

```kotlin
when (val result = SecureStorageKey.from("session:access_token")) {
    is SdkResult.Success -> readStoredToken(result.data)
    is SdkResult.Failure -> showStorageKeyError(result.error)
}
```

Use `SdkResult.Success` for completed operations and `SdkResult.Failure` for recoverable SDK failures such as invalid input, missing permissions, provider errors, missing configuration, unavailable services, or unsupported platform state.

## Errors

SDK modules expose typed error information through `SdkError`. Provider modules map vendor-specific failures into SDK errors so app layers can make decisions without depending on Firebase, Google, OkHttp, or WorkManager exception types.

## Logging

The `logging` module provides `SdkLogger`, `NoOpSdkLogger`, and `AndroidSdkLogger`.

```kotlin
val logger = AndroidSdkLogger(isEnabled = BuildConfig.DEBUG)
```

Use `NoOpSdkLogger` when a feature should run silently. Provider implementations can accept `SdkLogger` without forcing apps to adopt a specific logging framework.

## Coroutines And Flow

Asynchronous SDK operations use suspend functions and Flow where appropriate:

- One-shot work uses `suspend`.
- Long-lived state such as auth state, connectivity, remote config changes, notification token changes, or work status can use `Flow`.
- SDK code preserves coroutine cancellation where provider APIs allow it.

## Provider Boundaries

Provider-neutral modules define contracts and models. Provider modules implement those contracts using vendor SDKs.

For example:

- `auth-core` defines auth sessions and provider contracts.
- `auth-firebase` implements Firebase Authentication.
- `auth-google` implements Google Credential Manager sign-in.
- `auth-firebase-google` bridges Google credentials into Firebase Auth.

This keeps apps free to depend on contracts in shared layers and provider implementations at composition boundaries.
