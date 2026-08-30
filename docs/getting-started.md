# Getting Started

## Requirements

- Android min SDK: 24.
- Android compile SDK: 37.1.
- Kotlin: 2.4.10.
- Android Gradle Plugin: 9.3.1.
- Java compatibility: Java 11.

Provider-specific credentials and configuration stay in the consuming app. For example, Firebase `google-services.json`, OAuth client IDs, notification settings, and app signing fingerprints are not owned by SDK modules.

## Repository Setup

Add Maven Central to your dependency repositories:

```kotlin
repositories {
    google()
    mavenCentral()
}
```

Then define the SDK version once:

```kotlin
val lanternVersion = "0.1.1"
```

## Pick The Modules You Need

Most apps should not install every module. Start with the provider-neutral module for a feature, then add a provider implementation only when your app uses that provider.

For a small foundation layer:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-core:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-logging:$lanternVersion")
```

For Firebase authentication:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase:$lanternVersion")
```

For Google sign-in backed by Firebase:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-auth-google:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-auth-firebase-google:$lanternVersion")
```

For app-local storage:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-secure-storage:$lanternVersion")
```

For networking:

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-network-okhttp:$lanternVersion")
```

## First SDK Result

SDK operations return expected success and failure states as `SdkResult<T>`:

```kotlin
val keyResult = SecureStorageKey.from("session:access_token")

when (keyResult) {
    is SdkResult.Success -> {
        val key = keyResult.data
    }
    is SdkResult.Failure -> {
        val error = keyResult.error
    }
}
```

This keeps provider errors predictable at app call sites without requiring every feature to throw provider-specific exceptions.

## First Provider Call

Provider modules follow the same result shape. For example, Firebase anonymous auth returns either a normalized Lantern session or an SDK error:

```kotlin
val authProvider = FirebaseAuthProvider()

when (val result = authProvider.signInAnonymously()) {
    is SdkResult.Success -> {
        val session = result.data
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

Firebase setup still belongs to the app module: add `google-services.json`, enable the Firebase provider you use, and register signing fingerprints where required.

## Next Steps

- Use [Artifacts](artifacts.md) for copy-ready dependency coordinates.
- Use [Module Guide](modules.md) to pick modules by feature area.
- Use [Core Patterns](core-patterns.md) for result handling, errors, logging, coroutines, and provider boundaries.
