# Secure Storage

`secure-storage` provides validated keys, key-value storage, and token storage.

```kotlin
implementation("com.norbertotaveras.mobilefoundation:mobilefoundation-secure-storage:$mobileFoundationVersion")
```

## Use It For

- Validating storage keys before persistence.
- Reading and writing string values through `SecureKeyValueStore`.
- Saving, reading, removing, and clearing tokens through `SecureTokenStore`.
- Using a DataStore-backed default implementation.

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
        namespace = "mobile_foundation",
        allowEmptyValues = false
    )
)
```

## Security Note

The current DataStore implementation stores app-local values and is designed to be paired with app-specific encryption policy when required.

## Boundaries

Provider credentials, Firebase JSON files, OAuth client IDs, and app secrets stay in the consuming app. The SDK provides storage primitives, not application secret ownership.
