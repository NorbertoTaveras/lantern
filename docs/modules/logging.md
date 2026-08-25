# Logging

`logging` provides a small SDK logging abstraction.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-logging:$lanternVersion")
```

## Use It For

- Passing a logger into SDK providers.
- Logging provider failures without binding app code to a logging framework.
- Disabling logs with `NoOpSdkLogger`.
- Emitting Android Logcat messages with `AndroidSdkLogger`.

## Basic Usage

```kotlin
val logger = AndroidSdkLogger(isEnabled = BuildConfig.DEBUG)

logger.debug("Starting sign-in flow.")
logger.error("Sign-in failed.", throwable)
```

Use `NoOpSdkLogger` in production paths where no SDK logging should occur:

```kotlin
val logger = NoOpSdkLogger()
```

## Log Levels

`LogLevel` models the severity used by SDK loggers. Provider implementations can use the logger without deciding how the app stores, forwards, or filters logs.

## Boundaries

The logging module is provider-neutral. It must not depend on Firebase, Google, networking, storage, or app UI.
