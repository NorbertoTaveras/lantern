# Module mobilefoundation-secure-storage

Key-value and token storage contracts with a DataStore-backed app-local implementation.

# Package com.norbertotaveras.mobilefoundation.securestorage

Defines storage keys, storage configuration, key-value storage, token storage, token types, token expiration helpers, and a DataStore-backed implementation.

The provided DataStore implementation is not encrypted by default. Apps that store highly sensitive values should encrypt values before writing them, provide an encrypted store implementation, or choose a platform-backed secret store that matches the app threat model.
