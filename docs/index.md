# Mobile Foundation SDK

<p>
  <a href="https://github.com/NorbertoTaveras/android_mobilefoundation_framework/actions/workflows/android.yml">
    <img alt="Android CI" src="https://img.shields.io/badge/Android%20CI-configured-blue.svg" />
  </a>
  <a href="https://github.com/NorbertoTaveras/android_mobilefoundation_framework/actions/workflows/publish-docs.yml">
    <img alt="Docs" src="https://img.shields.io/badge/docs-live-success.svg" />
  </a>
  <img alt="License" src="https://img.shields.io/badge/license-Apache--2.0-blue.svg" />
  <img alt="Kotlin" src="https://img.shields.io/badge/kotlin-2.4.10-7F52FF.svg" />
  <img alt="Min SDK" src="https://img.shields.io/badge/min%20SDK-24-3DDC84.svg" />
  <img alt="Maven Central" src="https://img.shields.io/badge/Maven%20Central-pending-lightgrey.svg" />
</p>

Mobile Foundation SDK is a modular Android foundation toolkit for apps that need reusable, provider-aware building blocks for common product infrastructure.

It gives Android apps a consistent SDK layer for authentication, logging, permissions, secure storage, networking, remote config, feature flags, notifications, media picking, analytics, deep links, background work, and app versioning.

[:material-rocket-launch: Get Started](getting-started.md){ .md-button .md-button--primary }
[:material-view-module: Browse Modules](modules.md){ .md-button }
[:material-package-variant-closed: Artifacts](artifacts.md){ .md-button }
[:material-github: View Source](https://github.com/NorbertoTaveras/android_mobilefoundation_framework){ .md-button }

!!! warning "Pre-1.0 SDK"
    Mobile Foundation SDK is being prepared for public Maven Central publishing. APIs should be treated as early release APIs until the first stable version is published.

## Quick Install

Add Maven Central, then install only the modules your app needs:

```kotlin
repositories {
    google()
    mavenCentral()
}

val mobileFoundationVersion = "0.1.0"

implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-sdk-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-logging:$mobileFoundationVersion")
```

For feature-specific setup, start with the [Module Guide](modules.md) or jump into [Getting Started](getting-started.md).

## What The SDK Provides

<div class="grid cards" markdown>

-   :material-layers-triple: **Modular By Default**

    Install only the modules your app needs. Core contracts stay separate from Firebase, Google, OkHttp, WorkManager, and other provider integrations.

-   :material-language-kotlin: **Kotlin-First APIs**

    SDK APIs use typed models, coroutines, Flow, explicit interfaces, and predictable `SdkResult` handling.

-   :material-shield-check: **Provider Boundaries**

    Firebase, Google, OAuth, notification, and app signing configuration stay in the consuming application.

-   :material-cellphone-cog: **UI-Independent SDK**

    SDK modules do not depend on Compose UI, so apps can use Compose, Views, or mixed UI stacks.

</div>

## Current Status

Public releases are intended for Maven Central. The Maven Central badge will be replaced with the published artifact version after the first public release.

| Area | Status |
| --- | --- |
| Public docs | Live through GitHub Pages |
| CI validation | Android CI and docs publishing workflows |
| Release publishing | Prepared for Maven Central |
| Version line | Pre-1.0 |

## Documentation Structure

| Page | Purpose |
| --- | --- |
| [Getting Started](getting-started.md) | Installation, requirements, dependency selection, and first result handling. |
| [Artifacts](artifacts.md) | Maven group, artifact names, and copy-ready dependency snippets. |
| [Core Patterns](core-patterns.md) | `SdkResult`, `SdkError`, logging, coroutines, Flow, and provider boundaries. |
| [Module Guide](modules.md) | All SDK modules and links to detailed module pages. |
| [Authentication](authentication.md) | Firebase Auth, Google sign-in, and Firebase + Google setup. |
| [Integrations](integrations.md) | Permissions, storage, networking, config, notifications, analytics, deep links, work, and versioning. |
| [Publishing](publishing.md) | Public release and Maven Central expectations. |

## Design Principles

- Keep SDK library modules UI-independent.
- Keep provider-neutral contracts separate from provider integrations.
- Keep Firebase and Google configuration in the consuming app.
- Prefer small public APIs over broad utility buckets.
- Return expected failures as values through `SdkResult`.
- Keep app-specific runtime decisions in the application layer.
