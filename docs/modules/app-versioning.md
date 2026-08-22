# App Versioning

`app-versioning` reads the installed app version and evaluates update policy.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-app-versioning:$lanternVersion")
```

## Use It For

- Reading the current Android app version.
- Providing static version values in non-Android layers or tests.
- Comparing current, minimum supported, and latest versions.
- Modeling optional, recommended, and required update states.

## Current Version

```kotlin
val provider: AppVersionProvider = AndroidAppVersionProvider(context)

when (val result = provider.getCurrentVersion()) {
    is SdkResult.Success -> {
        val version = result.data
    }
    is SdkResult.Failure -> {
        val error = result.error
    }
}
```

## Update Policy

```kotlin
val evaluator = DefaultAppUpdatePolicyEvaluator()

val state = evaluator.evaluate(
    currentVersion = AppVersion(major = 1, minor = 1, patch = 0),
    policy = AppUpdatePolicy(
        minimumSupportedVersion = AppVersion(major = 1, minor = 0, patch = 0),
        latestVersion = AppVersion(major = 1, minor = 2, patch = 0)
    )
)
```

## Boundaries

The SDK evaluates version policy. The app decides how to display update prompts, force-update screens, or store remote update configuration.
