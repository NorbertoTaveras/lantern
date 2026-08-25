# Feature Flags

`feature-flags` evaluates typed feature flags from static defaults or remote config.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-feature-flags:$lanternVersion")
```

## Use It For

- Defining typed feature flag keys.
- Providing static feature flag values.
- Reading feature flags from `RemoteConfigProvider`.
- Tracking whether a value came from static defaults, remote config, or fallback behavior.

## Static Flags

```kotlin
val newHomeFlag = FeatureFlag(
    key = FeatureFlagKey.unsafe("new_home"),
    defaultValue = FeatureFlagValue.BooleanValue(false)
)

val provider = StaticFeatureFlagProvider(
    initialValues = mapOf(
        newHomeFlag.key to FeatureFlagValue.BooleanValue(true)
    )
)

when (val result = provider.evaluate(newHomeFlag)) {
    is SdkResult.Success -> {
        val evaluation = result.data
        val enabled = evaluation.isEnabled()
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

Static providers can also be updated at runtime for local demos or tests:

```kotlin
provider.update(
    values = mapOf(
        newHomeFlag.key to FeatureFlagValue.BooleanValue(false)
    )
)
```

## Defaults

```kotlin
val defaults = FeatureFlagDefaults(
    values = mapOf(
        FeatureFlagKey.unsafe("new_home") to FeatureFlagValue.BooleanValue(false),
        FeatureFlagKey.unsafe("paywall_variant") to FeatureFlagValue.StringValue("control")
    )
)

val remoteDefaults = defaults.toRemoteConfigDefaults()
```

Use defaults when the app needs one source of fallback values that can be shared with remote config setup.

## Remote Config Flags

```kotlin
val featureFlagProvider = RemoteConfigFeatureFlagProvider(remoteConfigProvider)

val enabledResult = featureFlagProvider.isEnabled(
    FeatureFlag(
        key = FeatureFlagKey.unsafe("new_home"),
        defaultValue = FeatureFlagValue.BooleanValue(false)
    )
)

val snapshotResult = featureFlagProvider.getSnapshot()
```

## Observing Updates

```kotlin
featureFlagProvider.updates.collect { snapshot ->
    val values = snapshot.values
}
```

## Boundaries

Feature flags should model app behavior decisions. The module does not own UI rollout, analytics experiments, or remote config provider setup.
