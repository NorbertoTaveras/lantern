package com.norbertotaveras.mobilefoundation.securestorage

data class SecureToken(
    val value: String,
    val type: SecureTokenType = SecureTokenType.Bearer,
    val expiresAtEpochMillis: Long? = null,
    val metadata: Map<String, String> = emptyMap()
) {
    fun isExpired(currentTimeMillis: Long): Boolean {
        return expiresAtEpochMillis?.let { currentTimeMillis >= it } ?: false
    }
}
