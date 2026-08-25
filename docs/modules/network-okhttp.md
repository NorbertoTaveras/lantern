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
- Mapping network failures.
- Observing connectivity state.

## Basic Client

```kotlin
val client = OkHttpNetworkClientFactory(
    config = NetworkConfig(
        defaultHeaders = mapOf("Accept" to "application/json")
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

## Retry

```kotlin
val client = OkHttpNetworkClientFactory().create(
    retryConfig = NetworkRetryConfig(maxRetries = 2)
)
```

## Logging

```kotlin
val client = OkHttpNetworkClientFactory()
    .createWithLogging(
        logger = AndroidSdkLogger(isEnabled = BuildConfig.DEBUG),
        loggingLevel = NetworkLoggingLevel.Basic
    )
```

Request and response bodies are not logged by the SDK logger.

## Boundaries

This module does not own API endpoints, Retrofit services, auth session state, or app-specific networking policy. Apps compose those pieces around the provided OkHttp utilities.
