# Artifacts

Mobile Foundation SDK publishes each SDK module as a separate Maven artifact. Apps should depend only on the modules they use.

!!! warning "Pre-1.0 artifacts"
    Public Maven Central releases are still pending. The coordinates below describe the intended public artifact names for stable releases.

## Version

Define the SDK version once in your Gradle build:

```kotlin
val mobileFoundationVersion = "0.1.0"
```

## Repository

Stable releases are intended to resolve from Maven Central:

```kotlin
repositories {
    google()
    mavenCentral()
}
```

## Maven Group

```text
com.norbertotaveras.mobilefoundation
```

## Core Artifacts

| Module | Artifact |
| --- | --- |
| `sdk-core` | `mobilefoundation-sdk-core` |
| `logging` | `mobilefoundation-logging` |

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-sdk-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-logging:$mobileFoundationVersion")
```

## Authentication Artifacts

| Module | Artifact |
| --- | --- |
| `auth-core` | `mobilefoundation-auth-core` |
| `auth-firebase` | `mobilefoundation-auth-firebase` |
| `auth-google` | `mobilefoundation-auth-google` |
| `auth-firebase-google` | `mobilefoundation-auth-firebase-google` |

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-google:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase-google:$mobileFoundationVersion")
```

## App Foundation Artifacts

| Module | Artifact |
| --- | --- |
| `permissions` | `mobilefoundation-permissions` |
| `secure-storage` | `mobilefoundation-secure-storage` |
| `network-okhttp` | `mobilefoundation-network-okhttp` |
| `remote-config` | `mobilefoundation-remote-config` |
| `remote-config-firebase` | `mobilefoundation-remote-config-firebase` |
| `feature-flags` | `mobilefoundation-feature-flags` |
| `notifications` | `mobilefoundation-notifications` |
| `notifications-firebase` | `mobilefoundation-notifications-firebase` |
| `media-picker` | `mobilefoundation-media-picker` |
| `analytics` | `mobilefoundation-analytics` |
| `analytics-firebase` | `mobilefoundation-analytics-firebase` |
| `deep-links` | `mobilefoundation-deep-links` |
| `background-work` | `mobilefoundation-background-work` |
| `app-versioning` | `mobilefoundation-app-versioning` |

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-permissions:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-secure-storage:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-network-okhttp:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config-firebase:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-feature-flags:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-notifications:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-notifications-firebase:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-media-picker:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-analytics:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-analytics-firebase:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-deep-links:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-background-work:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-app-versioning:$mobileFoundationVersion")
```

## Selection Guidance

Start with the provider-neutral artifact for a feature. Add provider-specific artifacts only when your app uses that provider.

For example:

- Use `mobilefoundation-auth-core` for shared auth models and contracts.
- Add `mobilefoundation-auth-firebase` when the app uses Firebase Authentication.
- Add `mobilefoundation-auth-google` when the app uses Google sign-in.
- Add `mobilefoundation-auth-firebase-google` when Google sign-in should authenticate with Firebase.

The sample app module is not published.
