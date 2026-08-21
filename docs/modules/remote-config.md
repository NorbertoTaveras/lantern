# Remote Config

`remote-config` defines provider-neutral remote configuration contracts. `remote-config-firebase` implements those contracts with Firebase Remote Config.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-remote-config-firebase:$mobileFoundationVersion")
```

## Use It For

- Defining typed remote config keys.
- Supplying defaults.
- Fetching and activating values.
- Reading a config snapshot.
- Keeping app code dependent on `RemoteConfigProvider` instead of Firebase directly.

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

## Settings

Use `RemoteConfigSettings` for SDK-level fetch settings, then let the Firebase implementation map those values to Firebase Remote Config.

## Boundaries

Firebase project configuration stays in the app. The core `remote-config` module does not depend on Firebase.
