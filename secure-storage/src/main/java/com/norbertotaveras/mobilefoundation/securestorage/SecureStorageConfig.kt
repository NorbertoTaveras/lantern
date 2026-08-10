package com.norbertotaveras.mobilefoundation.securestorage

data class SecureStorageConfig(
    val namespace: String = DEFAULT_NAMESPACE,
    val allowEmptyValues: Boolean = true
) {
    init {
        val normalizedNamespace = namespace.trim()
        require(normalizedNamespace.isNotEmpty()) { "Secure storage namespace cannot be blank." }
        require(normalizedNamespace.length <= MAX_NAMESPACE_LENGTH) {
            "Secure storage namespace cannot exceed $MAX_NAMESPACE_LENGTH characters."
        }
        require(NAMESPACE_PATTERN.matches(normalizedNamespace)) {
            "Secure storage namespace can only contain letters, numbers, periods, underscores, and hyphens."
        }
    }

    companion object {
        const val DEFAULT_NAMESPACE = "mobile_foundation_secure_storage"

        private const val MAX_NAMESPACE_LENGTH = 80
        private val NAMESPACE_PATTERN = Regex("^[A-Za-z0-9._-]+$")
    }
}
