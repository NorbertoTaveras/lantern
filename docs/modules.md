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

## Sample App

The `app` module is a Compose sample application. It demonstrates SDK features and owns runtime app configuration such as Firebase `google-services.json`.

The sample app is not published as part of the SDK.
