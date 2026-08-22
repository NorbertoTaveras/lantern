# Artifacts

Lantern publishes each SDK module as a separate Maven artifact. Apps should depend only on the modules they use.

!!! warning "Pre-1.0 artifacts"
    Public Maven Central releases are still pending. The coordinates below describe the intended public artifact names for stable releases.

## Version

Define the SDK version once in your Gradle build:

```kotlin
val lanternVersion = "0.1.0"
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
com.norbertotaveras.lantern
```

## Core Artifacts

| Module | Artifact |
| --- | --- |
| `sdk-core` | `lantern-core` |
| `logging` | `lantern-logging` |

```kotlin
implementation("com.norbertotaveras.lantern:lantern-core:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-logging:$lanternVersion")
```

## Authentication Artifacts

| Module | Artifact |
| --- | --- |
| `auth-core` | `lantern-auth-core` |
| `auth-firebase` | `lantern-auth-firebase` |
| `auth-google` | `lantern-auth-google` |
| `auth-firebase-google` | `lantern-auth-firebase-google` |

```kotlin
implementation("com.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-google:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase-google:$lanternVersion")
```

## App Foundation Artifacts

| Module | Artifact |
| --- | --- |
| `permissions` | `lantern-permissions` |
| `secure-storage` | `lantern-secure-storage` |
| `network-okhttp` | `lantern-network-okhttp` |
| `remote-config` | `lantern-remote-config` |
| `remote-config-firebase` | `lantern-remote-config-firebase` |
| `feature-flags` | `lantern-feature-flags` |
| `notifications` | `lantern-notifications` |
| `notifications-firebase` | `lantern-notifications-firebase` |
| `media-picker` | `lantern-media-picker` |
| `analytics` | `lantern-analytics` |
| `analytics-firebase` | `lantern-analytics-firebase` |
| `deep-links` | `lantern-deep-links` |
| `background-work` | `lantern-background-work` |
| `app-versioning` | `lantern-app-versioning` |

```kotlin
implementation("com.norbertotaveras.lantern:lantern-permissions:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-secure-storage:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-network-okhttp:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-remote-config:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-remote-config-firebase:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-feature-flags:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-notifications:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-notifications-firebase:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-media-picker:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-analytics:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-analytics-firebase:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-deep-links:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-background-work:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-app-versioning:$lanternVersion")
```

## Selection Guidance

Start with the provider-neutral artifact for a feature. Add provider-specific artifacts only when your app uses that provider.

For example:

- Use `lantern-auth-core` for shared auth models and contracts.
- Add `lantern-auth-firebase` when the app uses Firebase Authentication.
- Add `lantern-auth-google` when the app uses Google sign-in.
- Add `lantern-auth-firebase-google` when Google sign-in should authenticate with Firebase.

The sample app module is not published.
