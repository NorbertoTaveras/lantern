# Module Guide

## Core

| Module | Purpose |
| --- | --- |
| `sdk-core` | Shared primitives such as `SdkResult`, `SdkError`, `SdkConfig`, environment modeling, and dispatcher providers. |
| `logging` | SDK logging abstraction with no-op and Android Logcat implementations. |

## Authentication

| Module | Purpose |
| --- | --- |
| `auth-core` | Provider-neutral authentication contracts, sessions, users, tokens, provider types, and auth state. |
| `auth-firebase` | Firebase Authentication provider implementation for anonymous, email/password, session lookup, sign-out, and auth state observation. |
| `auth-google` | Google sign-in through Android Credential Manager and Google ID token retrieval. |
| `auth-firebase-google` | Google sign-in followed by Firebase credential exchange. |

## App Foundation

| Module | Purpose |
| --- | --- |
| `permissions` | Android-version-aware runtime permission checks, requests, rationale, and denied-state modeling. |
| `secure-storage` | Validated secure storage keys, key-value storage, and token storage. |
| `network-okhttp` | OkHttp factory, default headers, auth header interceptor, logging, retry, connectivity, and error mapping. |
| `remote-config` | Provider-neutral remote config keys, values, defaults, snapshots, and fetch/activate contracts. |
| `remote-config-firebase` | Firebase Remote Config implementation. |
| `feature-flags` | Typed feature flags backed by static providers or remote config providers. |
| `notifications` | Provider-neutral notification payloads, tokens, topics, channels, permissions, and deep-link abstractions. |
| `notifications-firebase` | Firebase Messaging token and notification helpers. |
| `media-picker` | Typed wrapper around Android Photo Picker requests and results. |
| `analytics` | Provider-neutral analytics events, parameters, users, and no-op provider. |
| `analytics-firebase` | Firebase Analytics implementation. |
| `deep-links` | URI parsing, typed deep-link models, and scheme/host allow-listing. |
| `background-work` | WorkManager-backed scheduling, cancellation, querying, and observation. |
| `app-versioning` | Current app version lookup and update policy evaluation. |

## Detailed Module Pages

- [SDK Core](modules/sdk-core.md)
- [Logging](modules/logging.md)
- [Auth Core](modules/auth-core.md)
- [Firebase Auth](modules/auth-firebase.md)
- [Google Auth](modules/auth-google.md)
- [Firebase Google Auth](modules/auth-firebase-google.md)
- [Permissions](modules/permissions.md)
- [Secure Storage](modules/secure-storage.md)
- [Network OkHttp](modules/network-okhttp.md)
- [Remote Config](modules/remote-config.md)
- [Feature Flags](modules/feature-flags.md)
- [Notifications](modules/notifications.md)
- [Media Picker](modules/media-picker.md)
- [Analytics](modules/analytics.md)
- [Deep Links](modules/deep-links.md)
- [Background Work](modules/background-work.md)
- [App Versioning](modules/app-versioning.md)
