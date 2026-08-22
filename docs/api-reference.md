# API Reference

Lantern publishes generated API reference documentation from source KDoc.

!!! tip "Generated from source"
    The generated reference is built from source KDoc. Use it together with the conceptual module
    guides on this site.

[Open Generated API Reference](generated/api/index.html){ .md-button .md-button--primary }

## Where To Start

Use these pages first:

- [Artifacts](artifacts.md) for Maven coordinates.
- [Module Guide](modules.md) for module selection.
- [Core Patterns](core-patterns.md) for shared result, error, logging, coroutine, and provider conventions.
- [Authentication](authentication.md) for auth-specific setup and usage.
- [Integrations](integrations.md) for app foundation modules.

## Current API Surface

The SDK is organized into focused modules:

| Area | Modules |
| --- | --- |
| Core | `sdk-core`, `logging` |
| Authentication | `auth-core`, `auth-firebase`, `auth-google`, `auth-firebase-google` |
| App foundation | `permissions`, `secure-storage`, `network-okhttp`, `remote-config`, `feature-flags`, `notifications`, `media-picker`, `analytics`, `deep-links`, `background-work`, `app-versioning` |

Each detailed module page lists its purpose, artifact, setup notes, usage examples, and boundaries.
