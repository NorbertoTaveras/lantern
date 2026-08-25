# Deep Links

`deep-links` parses URI strings into typed models and can reject unexpected schemes or hosts.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-deep-links:$lanternVersion")
```

## Use It For

- Parsing URI strings safely.
- Restricting allowed schemes.
- Restricting allowed hosts.
- Modeling parsed paths and query parameters.
- Resolving Android intents into deep links.

## Basic Usage

```kotlin
val parser = DefaultDeepLinkParser(
    config = DeepLinkConfig(
        allowedSchemes = setOf("myapp"),
        allowedHosts = setOf("open")
    )
)

val result = parser.parse("myapp://open/profile?id=123")
```

## Android Intents

```kotlin
val resolver = AndroidDeepLinkIntentResolver(parser)
val result = resolver.resolve(intent)
```

## Boundaries

This module does not own navigation UI. Apps map parsed deep links to their navigation graph or routing layer.
