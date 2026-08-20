# Publishing

Stable Mobile Foundation SDK releases are intended to be consumed from Maven Central.

## Release Versioning

Public releases use semantic versioning:

```text
0.1.0
0.2.0
1.0.0
```

Until the SDK reaches `1.0.0`, minor versions may include API changes as public contracts are finalized.

## Maven Coordinates

All public artifacts use this group:

```text
com.norbertotaveras.mobilefoundation
```

Artifacts are named with the `mobilefoundation-` prefix:

```text
mobilefoundation-sdk-core
mobilefoundation-auth-core
mobilefoundation-auth-firebase
mobilefoundation-network-okhttp
```

## Release Notes

GitHub Releases are expected to include generated release notes and GitHub-provided source archives. Maven Central is the canonical destination for binary SDK artifacts.

## Consumer Expectations

Consuming apps should:

- Use Maven Central.
- Depend only on needed modules.
- Keep provider credentials and configuration in the app.
- Treat pre-1.0 APIs as early release APIs.
