package com.norbertotaveras.mobilefoundation.securestorage

data class SecureStorageConfig(
    val namespace: String = DEFAULT_NAMESPACE,
    val allowEmptyValues: Boolean = true
) {
    companion object {
        const val DEFAULT_NAMESPACE = "mobile_foundation_secure_storage"
    }
}
