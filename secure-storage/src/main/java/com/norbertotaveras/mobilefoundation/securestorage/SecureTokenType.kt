package com.norbertotaveras.mobilefoundation.securestorage

/**
 * Category for a [SecureToken].
 */
enum class SecureTokenType {
    /**
     * Access token typically sent as a bearer credential.
     */
    Bearer,
    /**
     * Provider identity token, such as an OpenID Connect ID token.
     */
    IdToken,
    /**
     * Long-lived token used to refresh a session.
     */
    RefreshToken,
    /**
     * Caller-defined token type.
     */
    Custom
}
