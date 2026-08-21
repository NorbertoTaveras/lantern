# Feature Flags

`feature-flags` evaluates typed feature flags from static defaults or remote config.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-feature-flags:$mobileFoundationVersion")
```

## Use It For

- Defining typed feature flag keys.
- Providing static feature flag values.
- Reading feature flags from `RemoteConfigProvider`.
- Tracking whether a value came from static defaults, remote config, or fallback behavior.

## Static Flags

```kotlin
val provider = StaticFeatureFlagProvider(
    defaults = FeatureFlagDefaults(
        flags = mapOf(
            FeatureFlagKey.unsafe("new_home") to FeatureFlagValue.BooleanValue(false)
        )
    )
)
```

## Remote Config Flags

```kotlin
val featureFlagProvider = RemoteConfigFeatureFlagProvider(remoteConfigProvider)

val enabled = featureFlagProvider.isEnabled(
    FeatureFlag(
        key = FeatureFlagKey.unsafe("new_home"),
        defaultValue = FeatureFlagValue.BooleanValue(false)
    )
)
```

## Boundaries

Feature flags should model app behavior decisions. The module does not own UI rollout, analytics experiments, or remote config provider setup.
