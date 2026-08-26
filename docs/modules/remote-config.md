# Remote Config

`remote-config` defines provider-neutral remote configuration contracts. `remote-config-firebase` implements those contracts with Firebase Remote Config.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-remote-config:$lanternVersion")
implementation("io.github.norbertotaveras.lantern:lantern-remote-config-firebase:$lanternVersion")
```

## Use It For

- Defining typed remote config keys.
- Supplying defaults.
- Fetching and activating values.
- Reading a config snapshot.
- Keeping app code dependent on `RemoteConfigProvider` instead of Firebase directly.

!!! info "Firebase Remote Config setup"
    `remote-config-firebase` expects Firebase to be initialized by the consuming app. Add
    `google-services.json` to the app module, apply the Google Services plugin in the app, and
    configure Remote Config defaults, fetch intervals, and console values in your Firebase project.

## Basic Usage

```kotlin
val provider: RemoteConfigProvider = FirebaseRemoteConfigProvider()

provider.setDefaults(
    RemoteConfigDefaults(
        values = mapOf(
            RemoteConfigKey.unsafe("new_home") to RemoteConfigValue.BooleanValue(false)
        )
    )
)

provider.fetchAndActivate()
val snapshot = provider.getSnapshot()
```

Handle results explicitly so fetch or activation failures do not look like missing feature data:

```kotlin
when (val result = provider.fetchAndActivate()) {
    is SdkResult.Success -> {
        val changed = result.data
    }
    is SdkResult.Failure -> {
        logger.error("Remote config refresh failed: ${result.error.code}")
    }
}
```

## Settings

Use `RemoteConfigSettings` for SDK-level fetch settings, then let the Firebase implementation map those values to Firebase Remote Config.

```kotlin
val provider = FirebaseRemoteConfigProvider(
    config = FirebaseRemoteConfigProviderConfig(
        settings = RemoteConfigSettings(
            minimumFetchIntervalMillis = 60 * 60 * 1000L,
            fetchTimeoutMillis = 30 * 1000L
        )
    )
)
```

For debug builds, use a lower fetch interval in the consuming app so local testing does not wait for
production cache windows.

## Reading Values

```kotlin
val key = RemoteConfigKey.unsafe("new_home")

when (val result = provider.getValue(key)) {
    is SdkResult.Success -> {
        val enabled = when (val value = result.data) {
            is RemoteConfigValue.BooleanValue -> value.value
            else -> false
        }
    }
    is SdkResult.Failure -> {
        val enabled = false
    }
}
```

Use a snapshot when the app needs a point-in-time view of several values:

```kotlin
val snapshotResult = provider.getSnapshot()
```

## Boundaries

Firebase project configuration stays in the app. The core `remote-config` module does not depend on Firebase.
