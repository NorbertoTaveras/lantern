# Versioning And Migration

Mobile Foundation SDK versions are intended to be simple for consumers: choose a released version, use it consistently across modules, and read release notes before upgrading.

!!! warning "Pre-1.0 API stability"
    Until the SDK reaches `1.0.0`, public APIs may still change as module contracts are finalized.
    Release notes should call out breaking changes and migration steps when an upgrade requires app
    code changes.

## Version Format

Stable public releases use semantic versioning:

```text
MAJOR.MINOR.PATCH
```

Examples:

```text
0.1.0
0.2.0
1.0.0
```

## How To Choose A Version

Use the same Mobile Foundation version for every SDK module in your app:

```kotlin
val mobileFoundationVersion = "0.1.0"

implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-sdk-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-core:$mobileFoundationVersion")
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-auth-firebase:$mobileFoundationVersion")
```

Avoid mixing versions across modules unless a release note explicitly says it is supported.

## Upgrade Guidance

When upgrading:

1. Read the GitHub Release notes for the target version.
2. Update `mobileFoundationVersion`.
3. Sync Gradle.
4. Build your app.
5. Run the app flows that use changed modules.
6. Review any migration notes for renamed APIs, changed behavior, or new setup requirements.

## Patch, Minor, And Major Releases

| Version Change | Expected Meaning |
| --- | --- |
| Patch | Bug fixes, documentation fixes, and small compatibility improvements. |
| Minor before `1.0.0` | New features and possible API changes while contracts stabilize. |
| Minor after `1.0.0` | Backward-compatible feature additions. |
| Major | Breaking API or behavior changes. |

## Breaking Changes

Breaking changes may include:

- Renamed or removed public APIs.
- Changed method signatures.
- Changed default behavior.
- New required provider setup.
- Updated minimum Android, Kotlin, Gradle, Firebase, Google, or AndroidX requirements.

When a breaking change is released, the release notes should include:

- What changed.
- Why it changed.
- Which modules are affected.
- Before and after examples when useful.
- Required app-side migration steps.

## Provider Setup Changes

Provider setup is owned by the consuming app. If a release changes Firebase, Google, notification, analytics, or Android permission setup expectations, those requirements should be called out in the affected module docs and release notes.

## Where To Look

- [Artifacts](artifacts.md) for Maven coordinates.
- [Module Guide](modules.md) for module selection.
- [Authentication](authentication.md) for Firebase and Google setup.
- [Integrations](integrations.md) for app foundation modules.
- GitHub Releases for version-specific release notes.
