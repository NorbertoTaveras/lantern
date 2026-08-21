# API Reference

Mobile Foundation SDK API details currently live in source KDoc and module documentation.

!!! info "Generated API docs are planned"
    Public generated API reference docs are not published yet. Until generated reference docs are
    available, use this documentation site together with source KDoc in the GitHub repository.

## Where To Start

Use these pages first:

- [Artifacts](artifacts.md) for Maven coordinates.
- [Module Guide](modules.md) for module selection.
- [Core Patterns](core-patterns.md) for shared result, error, logging, coroutine, and provider conventions.
- [Authentication](authentication.md) for auth-specific setup and usage.
- [Integrations](integrations.md) for app foundation modules.

## Source KDoc

Public SDK classes, interfaces, value types, and provider implementations should carry concise KDoc where it clarifies:

- Purpose.
- Expected usage.
- Ownership boundaries.
- Provider setup requirements.
- Cancellation or Flow behavior.
- Error handling behavior.

KDoc should explain public APIs without restating obvious Kotlin syntax.

## Current API Surface

The SDK is organized into focused modules:

| Area | Modules |
| --- | --- |
| Core | `sdk-core`, `logging` |
| Authentication | `auth-core`, `auth-firebase`, `auth-google`, `auth-firebase-google` |
| App foundation | `permissions`, `secure-storage`, `network-okhttp`, `remote-config`, `feature-flags`, `notifications`, `media-picker`, `analytics`, `deep-links`, `background-work`, `app-versioning` |

Each detailed module page lists its purpose, artifact, setup notes, usage examples, and boundaries.

## Future Generated Reference

Generated API docs should eventually provide:

- Package-level navigation.
- Class and interface reference pages.
- Public constructor and function signatures.
- KDoc rendered from source.
- Module grouping.
- Links back to source.

The generated reference should complement, not replace, the conceptual docs on this site.

## What Belongs In Conceptual Docs

Keep setup and workflow guidance in this documentation site:

- Which module to install.
- Which provider setup belongs in the app.
- How modules fit together.
- How to upgrade versions.
- Which errors and states app code should expect.

Keep exhaustive signatures and member-level details in generated API reference docs once they exist.
