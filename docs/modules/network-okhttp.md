# Network OkHttp

`network-okhttp` provides an OkHttp client factory and common network utilities.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-network-okhttp:$lanternVersion")
```

## Use It For

- Creating configured OkHttp clients.
- Applying default headers.
- Adding bearer auth headers through `TokenProvider`.
- Adding retry behavior.
- Adding SDK network logging.
- Observing connectivity state.

## Basic Client

```kotlin
val client = OkHttpNetworkClientFactory(
    config = NetworkConfig(
        connectTimeoutMillis = 10_000,
        readTimeoutMillis = 30_000,
        defaultHeaders = mapOf(
            "Accept" to "application/json",
            "X-App-Platform" to "android"
        )
    )
).create()
```

## Auth Header

```kotlin
val tokenProvider = object : TokenProvider {
    override fun getAccessToken(): String? = currentAccessToken
}

val client = OkHttpNetworkClientFactory().create(
    tokenProvider = tokenProvider
)
```

`AuthHeaderInterceptor` preserves an existing `Authorization` header by default. Use that when an
individual request needs a one-off token or no auth header at all.

## Retry

```kotlin
val client = OkHttpNetworkClientFactory().create(
    retryConfig = NetworkRetryConfig(
        maxRetries = 2,
        initialDelayMillis = 250
    )
)
```

Retries are best for transient failures. Keep non-idempotent API behavior in mind before applying
retry policy broadly to a shared client.

## Logging

```kotlin
val client = OkHttpNetworkClientFactory()
    .createWithLogging(
        logger = AndroidSdkLogger(isEnabled = BuildConfig.DEBUG),
        loggingLevel = NetworkLoggingLevel.Basic
    )
```

Request and response bodies are not logged by the SDK logger.

## Connectivity

```kotlin
val monitor = AndroidNetworkMonitor(context)

monitor.connectivity.collect { state ->
    if (state.isUsable) {
        refreshData()
    } else {
        showOfflineState()
    }
}
```

`isUsable` means Android reports an available and validated network. Apps should still handle
request failures because connectivity can change between observation and request execution.

## Boundaries

This module does not own API endpoints, Retrofit services, auth session state, or app-specific networking policy. Apps compose those pieces around the provided OkHttp utilities.
