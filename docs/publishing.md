# Releases

Stable Lantern releases are intended to be consumed from Maven Central.

!!! info "Stable artifacts"
    Consumers should treat Maven Central as the source of stable SDK binaries.

## Release Versioning

Public releases use semantic versioning:

```text
0.1.0
0.2.0
1.0.0
```

Until the SDK reaches `1.0.0`, minor versions may include API changes as public contracts are finalized.

See [Versioning And Migration](versioning-migration.md) for upgrade and migration guidance.

## Maven Coordinates

All public artifacts use this group:

```text
io.github.norbertotaveras.lantern
```

Artifacts are named with the `lantern-` prefix:

```text
lantern-core
lantern-auth-core
lantern-auth-firebase
lantern-network-okhttp
```

## Release Notes

GitHub Releases are expected to include generated release notes and GitHub-provided source archives. Maven Central is the canonical destination for binary SDK artifacts.

Release notes should call out:

- Added modules or features.
- Behavior changes.
- Deprecated APIs.
- Breaking changes.
- Migration steps when app code must change.

## Consumer Expectations

Consuming apps should:

- Use Maven Central.
- Depend only on needed modules.
- Keep provider credentials and configuration in the app.
- Treat pre-1.0 APIs as early release APIs.
- Read release notes before upgrading.
