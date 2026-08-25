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
val lanternVersion = "0.1.0"
```

## Pick The Modules You Need

Most apps should not install every module. Start with the provider-neutral module for a feature, then add a provider implementation only when your app uses that provider.

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

## First Result

SDK operations return expected success and failure states as `SdkResult<T>`:

```kotlin
when (val result = operation()) {
    is SdkResult.Success -> {
        val value = result.data
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

This keeps provider errors predictable at app call sites without requiring every feature to throw provider-specific exceptions.
