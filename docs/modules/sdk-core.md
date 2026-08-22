# SDK Core

`sdk-core` contains the shared primitives used across Lantern modules.

```kotlin
implementation("com.norbertotaveras.lantern:lantern-core:$lanternVersion")
```

## Use It For

- Modeling SDK success and failure with `SdkResult`.
- Carrying typed SDK errors with `SdkError`.
- Sharing SDK environment and configuration values through `SdkConfig` and `Environment`.
- Supplying coroutine dispatchers through `DispatcherProvider`.

## Result Handling

Most SDK operations return `SdkResult<T>`:

```kotlin
when (val result = repository.load()) {
    is SdkResult.Success -> render(result.data)
    is SdkResult.Failure -> showError(result.error)
}
```

Expected provider failures should be handled as `SdkResult.Failure`. Coroutine cancellation should still be allowed to propagate.

## Error Modeling

`SdkError` gives consumers a stable SDK-level error shape:

```kotlin
val error = SdkError(
    code = "network_unavailable",
    message = "Network is unavailable."
)
```

Provider modules map Firebase, Google, OkHttp, WorkManager, and Android platform failures into this SDK error model.

## Dispatchers

`DispatcherProvider` centralizes coroutine dispatchers:

```kotlin
val dispatchers: DispatcherProvider = DefaultDispatcherProvider
```

SDK implementations use dispatchers for IO and default work while keeping APIs easy to test.

## Boundaries

`sdk-core` must stay dependency-light. It should not depend on provider SDKs, Android UI, Compose, Firebase, Google, OkHttp, or WorkManager.
