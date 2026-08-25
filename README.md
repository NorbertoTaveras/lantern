<p align="center">
  <img src="docs/assets/brand/lantern-banner.png" alt="Lantern - A modular Android foundation SDK" width="100%" />
</p>

<p align="center">
  <a href="https://github.com/NorbertoTaveras/lantern/actions/workflows/android.yml">
    <img alt="Android CI" src="https://github.com/NorbertoTaveras/lantern/actions/workflows/android.yml/badge.svg" />
  </a>
  <a href="https://github.com/NorbertoTaveras/lantern/actions/workflows/publish-docs.yml">
    <img alt="Docs" src="https://github.com/NorbertoTaveras/lantern/actions/workflows/publish-docs.yml/badge.svg" />
  </a>
  <a href="https://norbertotaveras.github.io/lantern/api-reference/">
    <img alt="API Reference" src="https://img.shields.io/badge/API%20Reference-Dokka-4B6BFF.svg" />
  </a>
  <a href="LICENSE">
    <img alt="License" src="https://img.shields.io/badge/license-Apache--2.0-blue.svg" />
  </a>
  <img alt="Kotlin" src="https://img.shields.io/badge/kotlin-2.4.10-7F52FF.svg" />
  <img alt="Min SDK" src="https://img.shields.io/badge/min%20SDK-24-3DDC84.svg" />
  <img alt="Maven Central" src="https://img.shields.io/badge/Maven%20Central-pending-lightgrey.svg" />
</p>

Lantern is a modular Android foundation toolkit for apps that need a clean, reusable starting point for common product infrastructure: authentication, logging, permissions, secure storage, networking, remote config, feature flags, notifications, media picking, analytics, deep links, background work, and app versioning.

The SDK is designed around small modules, Kotlin-first APIs, coroutines, Flow, typed models, and explicit provider boundaries. Apps can depend on only the features they need, while keeping vendor-specific integrations such as Firebase, Google, OkHttp, and WorkManager isolated from provider-neutral contracts.

## Why Lantern

- Use one consistent SDK style across common Android app foundations.
- Keep app code provider-neutral where possible.
- Add Firebase, Google, OkHttp, and WorkManager integrations only when needed.
- Handle expected failures through `SdkResult` instead of scattered provider exceptions.
- Keep UI out of SDK modules so the library works with Compose, Views, or mixed apps.
- Keep Firebase and Google configuration in the consuming application.

## Status

Lantern is pre-1.0. The project is being prepared for public Maven Central publishing, so APIs should be treated as early release APIs until the first stable version is published.

The Maven Central badge will be replaced with the published artifact version after the first public release.

## Documentation

Read the public docs at [norbertotaveras.github.io/lantern](https://norbertotaveras.github.io/lantern/).

Useful starting points:

- [Getting Started](https://norbertotaveras.github.io/lantern/getting-started/)
- [Module Guide](https://norbertotaveras.github.io/lantern/modules/)
- [API Reference](https://norbertotaveras.github.io/lantern/api-reference/)
- [Generated Dokka Reference](https://norbertotaveras.github.io/lantern/generated/api/)

The documentation source lives in [docs](docs/index.md). The generated Dokka reference is produced during the docs publishing workflow and is not committed as source.

## Requirements

- Android min SDK: 24.
- Android compile SDK: 37.1.
- Kotlin: 2.4.10.
- Android Gradle Plugin: 9.3.1.
- Java compatibility: Java 11.
- Firebase configuration, Google OAuth clients, notification setup, and other provider-specific app credentials stay in the consuming app.

## Installation

Add Maven Central to your dependency repositories:

```kotlin
repositories {
    google()
    mavenCentral()
}
```

Then add the Lantern modules your app needs:

```kotlin
val lanternVersion = "0.1.0"

implementation("com.norbertotaveras.lantern:lantern-core:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-logging:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-core:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-google:$lanternVersion")
implementation("com.norbertotaveras.lantern:lantern-auth-firebase-google:$lanternVersion")
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

Most apps should not install every module. Start with the provider-neutral module for the feature you need, then add provider implementations such as Firebase or OkHttp only where your app uses them.

## Module Guide

| Feature | Use These Modules | What You Get |
| --- | --- | --- |
| Core SDK primitives | `sdk-core` | Shared `SdkResult`, `SdkError`, config, environment, and dispatcher primitives. |
| Logging | `logging` | Provider-neutral logger with no-op and Android Logcat implementations. |
| Authentication contracts | `auth-core` | Auth sessions, user profiles, provider types, auth state, and provider contract. |
| Firebase Auth | `auth-firebase` | Firebase Auth sign-in, sign-out, session lookup, auth-state observation, and Firebase error mapping. |
| Google sign-in | `auth-google` | Android Credential Manager Google sign-in and Google ID token retrieval. |
| Firebase + Google auth | `auth-firebase-google` | Google sign-in followed by Firebase credential exchange. |
| Permissions | `permissions` | Android-version-aware permission checks, requests, rationale, and denied-state modeling. |
| Secure storage | `secure-storage` | Validated storage keys, key-value storage contracts, token storage, and a DataStore-backed app-local implementation. |
| Networking | `network-okhttp` | OkHttp factory, auth header interceptor, default headers, retry, logging, and error mapping. |
| Remote config | `remote-config`, `remote-config-firebase` | Provider-neutral remote config contracts plus Firebase Remote Config implementation. |
| Feature flags | `feature-flags` | Typed feature flags with static and remote-config-backed providers. |
| Notifications | `notifications`, `notifications-firebase` | Notification payloads, tokens, topics, channels, permissions, and Firebase Messaging integration. |
| Media picking | `media-picker` | Android Photo Picker wrapper with typed requests and results. |
| Analytics | `analytics`, `analytics-firebase` | Typed analytics events, values, users, properties, no-op provider, and Firebase Analytics implementation. |
| Deep links | `deep-links` | URI parsing, typed deep-link models, and scheme/host allow-listing. |
| Background work | `background-work` | WorkManager-backed scheduling, cancellation, querying, and observation. |
| App versioning | `app-versioning` | Current app version lookup and update policy evaluation. |

## Common Result Handling

SDK operations model expected success and failure states with `SdkResult<T>`:

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

Provider integrations still protect coroutine cancellation where applicable, but normal SDK failures are returned as values so app code can handle them predictably.

## Authentication

`auth-core` defines the provider-neutral contract:

```kotlin
val authProvider: AuthProvider = FirebaseAuthProvider()

when (val result = authProvider.signIn()) {
    is SdkResult.Success -> {
        val session = result.data
        val userId = session.userId
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

### Firebase Auth

Use `auth-firebase` when your app uses Firebase Authentication:

```kotlin
val firebaseAuth = FirebaseAuthProvider()

firebaseAuth.signInAnonymously()
firebaseAuth.signInWithEmailAndPassword(email, password)
firebaseAuth.createUserWithEmailAndPassword(email, password)
firebaseAuth.getCurrentSession()
firebaseAuth.observeAuthState()
firebaseAuth.signOut()
```

The provider maps Firebase users into Lantern `AuthSession` models and Firebase failures into SDK errors.

### Google Sign-In

Use `auth-google` when your app needs Google sign-in through Android Credential Manager:

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

The returned `GoogleAuthToken` contains the Google ID token your app or backend can verify.

### Firebase + Google

Use `auth-firebase-google` when Google sign-in should authenticate with Firebase:

```kotlin
val firebaseGoogleAuthProvider = FirebaseGoogleAuthProvider(
    context = context,
    config = FirebaseGoogleAuthConfig(
        serverClientId = webOAuthClientId,
        filterByAuthorizedAccounts = false,
        autoSelectEnabled = false
    )
)

val result = firebaseGoogleAuthProvider.signIn()
```

Firebase and Google configuration stays in your app:

- Add `google-services.json` to the app module.
- Enable the Firebase providers your app uses.
- Use the Web OAuth client ID as `serverClientId`.
- Register debug and release SHA fingerprints in Firebase/Google Cloud for Google sign-in.

## Permissions

`permissions` checks and requests runtime permissions while accounting for Android API differences:

```kotlin
val permissionManager = AndroidPermissionManager(
    context = context,
    requestLauncher = permissionRequestLauncher
)

val cameraState = permissionManager.check(SdkPermission.Camera)
val requestResult = permissionManager.request(SdkPermission.Camera)
```

Your app owns the permission launcher because permission dialogs are lifecycle and UI-bound. The SDK handles permission mapping, checking, result modeling, and rationale/permanent-denial state.

## Secure Storage

`secure-storage` provides validated keys and storage contracts:

```kotlin
val keyResult = SecureStorageKey.from("session:access_token")

if (keyResult is SdkResult.Success) {
    val store = DataStoreSecureKeyValueStore(context)
    store.putString(keyResult.data, "token-value")
    val token = store.getString(keyResult.data)
}
```

Use `SecureTokenStore` for token-specific storage flows and `SecureKeyValueStore` for general app values. The provided DataStore implementation is app-local storage, not encryption by default. Apps that store highly sensitive values should wrap values with an app-owned encryption policy or provide an encrypted `SecureKeyValueStore` implementation.

## Networking

`network-okhttp` creates configured OkHttp clients without coupling networking to a specific auth provider:

```kotlin
val tokenProvider = object : TokenProvider {
    override fun getAccessToken(): String? = currentAccessToken
}

val client = OkHttpNetworkClientFactory(
    config = NetworkConfig(
        defaultHeaders = mapOf("Accept" to "application/json")
    )
).create(
    tokenProvider = tokenProvider,
    retryConfig = NetworkRetryConfig(maxRetries = 2)
)
```

Enable SDK network logging when useful:

```kotlin
val client = OkHttpNetworkClientFactory()
    .createWithLogging(
        logger = AndroidSdkLogger(isEnabled = true),
        loggingLevel = NetworkLoggingLevel.Basic
    )
```

Request and response bodies are not logged by the SDK logger.

## Remote Config

`remote-config` defines provider-neutral config keys, values, defaults, snapshots, and fetch/activate behavior:

```kotlin
val remoteConfigProvider: RemoteConfigProvider = FirebaseRemoteConfigProvider()

remoteConfigProvider.setDefaults(
    RemoteConfigDefaults(
        values = mapOf(
            RemoteConfigKey.unsafe("new_home") to RemoteConfigValue.BooleanValue(false)
        )
    )
)

remoteConfigProvider.fetchAndActivate()
val snapshot = remoteConfigProvider.getSnapshot()
```

Use `remote-config-firebase` for Firebase Remote Config while keeping the rest of your app dependent on `RemoteConfigProvider`.

## Feature Flags

`feature-flags` evaluates typed flags from static defaults or remote config:

```kotlin
val featureFlagProvider = RemoteConfigFeatureFlagProvider(remoteConfigProvider)

val enabled = featureFlagProvider.isEnabled(
    FeatureFlag(
        key = FeatureFlagKey.unsafe("new_home"),
        defaultValue = FeatureFlagValue.BooleanValue(false)
    )
)
```

Flags can carry descriptions and metadata for app tooling while still evaluating through a small runtime contract.

## Notifications

`notifications` provides provider-neutral notification models and contracts. `notifications-firebase` adds Firebase Messaging support:

```kotlin
val parser = DefaultNotificationPayloadParser()
val payload = parser.parse(data = remoteMessage.data)

val tokenProvider = FirebaseMessagingTokenProvider()
val token = tokenProvider.getToken()
```

The core notification module also includes topic, channel, permission, token, payload, and deep-link abstractions.

## Media Picker

`media-picker` wraps Android Photo Picker in typed requests and results:

```kotlin
val request = MediaPickRequest(
    mediaTypes = setOf(MediaType.Image),
    selectionMode = MediaSelectionMode.Single
)

val result = mediaPicker.pick(request)
```

Use the Android picker implementation from app UI code where you can provide the required launcher.

## Analytics

`analytics` defines provider-neutral event tracking:

```kotlin
val analyticsProvider: AnalyticsProvider = FirebaseAnalyticsProvider(context)

analyticsProvider.track(
    AnalyticsEvent(
        name = AnalyticsEventName.unsafe("screen_view"),
        parameters = mapOf("screen" to AnalyticsValue.StringValue("home"))
    )
)
```

Use `NoOpAnalyticsProvider` when analytics should be disabled without branching all call sites.

## Deep Links

`deep-links` parses URI strings into typed models and can reject unexpected schemes or hosts:

```kotlin
val parser = DefaultDeepLinkParser(
    config = DeepLinkConfig(
        allowedSchemes = setOf("myapp"),
        allowedHosts = setOf("open")
    )
)

val deepLink = parser.parse("myapp://open/profile?id=123")
```

This keeps URI validation separate from navigation UI.

## Background Work

`background-work` schedules WorkManager work through explicit SDK requests:

```kotlin
val workName = BackgroundWorkName.unsafe("sync-profile")
val scheduler = WorkManagerBackgroundWorkScheduler(
    context = context,
    workerClasses = mapOf(workName to SyncProfileWorker::class.java)
)

scheduler.enqueue(
    BackgroundWorkRequest(
        name = workName,
        type = BackgroundWorkType.OneTime
    )
)
```

Worker classes stay in your app. The SDK schedules, cancels, queries, and observes work without owning app-specific worker behavior.

## App Versioning

`app-versioning` reads the installed app version and evaluates update policy:

```kotlin
val evaluator = DefaultAppUpdatePolicyEvaluator()

when (val currentVersion = AndroidAppVersionProvider(context).getCurrentVersion()) {
    is SdkResult.Success -> {
        val state = evaluator.evaluate(
            currentVersion = currentVersion.data,
            policy = AppUpdatePolicy(
                minimumSupportedVersion = AppVersion(major = 1, minor = 0, patch = 0),
                latestVersion = AppVersion(major = 1, minor = 2, patch = 0)
            )
        )
    }
    is SdkResult.Failure -> {
        val error = currentVersion.error
    }
}
```

Use this to centralize force-update, recommended-update, and supported-version decisions.

## Sample App

The repository includes a Compose sample app in the `app` module. It demonstrates SDK features and owns app runtime configuration such as Firebase `google-services.json`.

```bash
./gradlew :app:build
```

## Architecture Guarantees

- SDK modules are UI-independent.
- Compose UI belongs in the sample app, not library modules.
- Provider-neutral contracts stay separate from provider implementations.
- Firebase and Google configuration stay in the consuming app.
- SDK APIs favor explicit interfaces, typed models, coroutines, Flow, and `SdkResult`.
- SDK modules do not hardcode secrets or app-specific runtime config.

## Versioning

Stable releases are published to Maven Central using semantic versioning. Until the SDK reaches `1.0.0`, minor versions may include API changes as the public contracts are finalized.

## License

```text
Copyright 2026 Norberto Taveras

Licensed under the Apache License, Version 2.0.
```

See [LICENSE](LICENSE) for the full license text.
