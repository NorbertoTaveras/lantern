package com.norbertotaveras.mobilefoundation.securestorage

/**
 * Structured token value persisted by [SecureTokenStore].
 *
 * [value] must not be blank. [expiresAtEpochMillis] is optional because not every credential
 * has an SDK-visible expiration. [metadata] is intended for lightweight provider/session
 * attributes and should not be used as a general document store.
 */
data class SecureToken(
    /**
     * Raw credential or token string.
     */
    val value: String,
    /**
     * Token category used by callers to distinguish access, ID, refresh, or custom tokens.
     */
    val type: SecureTokenType = SecureTokenType.Bearer,
    /**
     * Optional absolute expiration time in epoch milliseconds.
     */
    val expiresAtEpochMillis: Long? = null,
    /**
     * Optional lightweight token metadata.
     */
    val metadata: Map<String, String> = emptyMap()
) {
    init {
        require(value.isNotBlank()) { "Secure token value cannot be blank." }
    }

    /**
     * Returns `true` when [currentTimeMillis] is at or after [expiresAtEpochMillis].
     *
     * Tokens without an expiration are treated as not expired.
     */
    fun isExpired(currentTimeMillis: Long): Boolean {
        return expiresAtEpochMillis?.let { currentTimeMillis >= it } ?: false
    }
}
