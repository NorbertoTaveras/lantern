# Integrations

## Permissions

`permissions` checks and requests runtime permissions while accounting for Android API differences.

```kotlin
val permissionManager = AndroidPermissionManager(
    context = context,
    requestLauncher = permissionRequestLauncher
)

val cameraState = permissionManager.check(SdkPermission.Camera)
val requestResult = permissionManager.request(SdkPermission.Camera)
```

The consuming app owns the permission launcher because permission dialogs are lifecycle and UI-bound.

## Secure Storage

`secure-storage` provides validated keys and storage contracts.

```kotlin
val keyResult = SecureStorageKey.from("session:access_token")

if (keyResult is SdkResult.Success) {
    val store = DataStoreSecureKeyValueStore(context)
    store.putString(keyResult.data, "token-value")
    val token = store.getString(keyResult.data)
}
```

Use `SecureTokenStore` for token-specific flows and `SecureKeyValueStore` for general app values.

## Networking

`network-okhttp` creates configured OkHttp clients without coupling networking to a specific auth provider.

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

## Remote Config And Feature Flags

Use `remote-config` for provider-neutral config access and `remote-config-firebase` for Firebase-backed values.

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

Feature flags can sit on top of remote config:

```kotlin
val featureFlagProvider = RemoteConfigFeatureFlagProvider(remoteConfigProvider)

val enabled = featureFlagProvider.isEnabled(
    FeatureFlag(
        key = FeatureFlagKey.unsafe("new_home"),
        defaultValue = FeatureFlagValue.BooleanValue(false)
    )
)
```

## Notifications

`notifications` provides provider-neutral notification models. `notifications-firebase` adds Firebase Messaging support.

```kotlin
val parser = DefaultNotificationPayloadParser()
val payload = parser.parse(data = remoteMessage.data)

val tokenProvider = FirebaseMessagingTokenProvider()
val token = tokenProvider.getToken()
```

## Media Picker

`media-picker` wraps Android Photo Picker in typed requests and results.

```kotlin
val request = MediaPickRequest(
    mediaTypes = setOf(MediaType.Image),
    selectionMode = MediaSelectionMode.Single
)

val result = mediaPicker.pick(request)
```

## Analytics

`analytics` defines provider-neutral tracking. `analytics-firebase` sends events to Firebase Analytics.

```kotlin
val analyticsProvider: AnalyticsProvider = FirebaseAnalyticsProvider(context)

analyticsProvider.track(
    AnalyticsEvent(
        name = AnalyticsEventName.unsafe("screen_view"),
        parameters = mapOf("screen" to AnalyticsValue.StringValue("home"))
    )
)
```

## Deep Links

`deep-links` parses URI strings into typed models and can reject unexpected schemes or hosts.

```kotlin
val parser = DefaultDeepLinkParser(
    config = DeepLinkConfig(
        allowedSchemes = setOf("myapp"),
        allowedHosts = setOf("open")
    )
)

val deepLink = parser.parse("myapp://open/profile?id=123")
```

## Background Work

`background-work` schedules WorkManager work through explicit SDK requests.

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

## App Versioning

`app-versioning` reads the installed app version and evaluates update policy.

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
