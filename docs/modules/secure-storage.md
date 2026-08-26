# Secure Storage

`secure-storage` provides validated keys, key-value storage contracts, token storage, and a DataStore-backed app-local implementation.

```kotlin
implementation("io.github.norbertotaveras.lantern:lantern-secure-storage:$lanternVersion")
```

## Use It For

- Validating storage keys before persistence.
- Reading and writing string values through `SecureKeyValueStore`.
- Saving, reading, removing, and clearing tokens through `SecureTokenStore`.
- Using a DataStore-backed default implementation for app-local persistence.

## Key-Value Storage

```kotlin
val store = DataStoreSecureKeyValueStore(context)
val keyResult = SecureStorageKey.from("session:access_token")

if (keyResult is SdkResult.Success) {
    store.putString(keyResult.data, "token-value")
    val token = store.getString(keyResult.data)
}
```

## Token Storage

```kotlin
val tokenStore = DefaultSecureTokenStore(store)

tokenStore.saveToken(
    key = SecureStorageKey.unsafe("session:token"),
    token = SecureToken(
        value = "token-value",
        type = SecureTokenType.Bearer
    )
)
```

## Configuration

```kotlin
val store = DataStoreSecureKeyValueStore(
    context = context,
    config = SecureStorageConfig(
        namespace = "lantern",
        allowEmptyValues = false
    )
)
```

Apps upgrading from Lantern 0.1.0 can pass
`SecureStorageConfig.LEGACY_MOBILE_FOUNDATION_NAMESPACE` when they need to keep reading values
written with the old default namespace.

## Security Note

The provided DataStore implementation is not encrypted by default. It stores app-local values through AndroidX Preferences DataStore and is designed as a foundation implementation that can be paired with app-owned encryption policy.

For highly sensitive secrets, encrypt values before writing them, provide an encrypted `SecureKeyValueStore` implementation, or use a platform-backed secret store that matches your app's threat model.

## Boundaries

Provider credentials, Firebase JSON files, OAuth client IDs, and app secrets stay in the consuming app. The SDK provides storage primitives, not application secret ownership.
