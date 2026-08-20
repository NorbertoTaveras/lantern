# Mobile Foundation SDK

Mobile Foundation SDK is a modular Android foundation toolkit for apps that need reusable, provider-aware building blocks for common product infrastructure.

The SDK is organized as small Android library modules. Apps can install only the pieces they need, while provider-specific integrations such as Firebase, Google, OkHttp, and WorkManager stay isolated from provider-neutral contracts.

## What The SDK Provides

- Shared SDK primitives for result handling, errors, environments, and dispatchers.
- Provider-neutral contracts for authentication, remote config, notifications, analytics, and app foundation workflows.
- Provider implementations for Firebase, Google Credential Manager, Firebase Messaging, Firebase Analytics, Firebase Remote Config, OkHttp, and WorkManager.
- Kotlin-first APIs that use coroutines, Flow, explicit interfaces, and typed models.
- UI-independent SDK modules that work with Compose, Views, or mixed Android apps.

## Current Status

Mobile Foundation SDK is pre-1.0. Public releases are intended for Maven Central, and APIs should be treated as early release APIs until the first stable version is published.

## Documentation Structure

- [Getting Started](getting-started.md) explains installation and dependency selection.
- [Core Patterns](core-patterns.md) explains common SDK result, error, logging, coroutine, and Flow conventions.
- [Module Guide](modules.md) lists every module and when to use it.
- [Authentication](authentication.md) covers Firebase Auth, Google sign-in, and Firebase + Google.
- [Integrations](integrations.md) covers permissions, storage, networking, config, notifications, analytics, deep links, background work, and app versioning.
- [Publishing](publishing.md) explains public release expectations from a consumer perspective.

## Design Principles

- Keep SDK library modules UI-independent.
- Keep provider-neutral contracts separate from provider integrations.
- Keep Firebase and Google configuration in the consuming app.
- Prefer small public APIs over broad utility buckets.
- Return expected failures as values through `SdkResult`.
- Keep app-specific runtime decisions in the application layer.
