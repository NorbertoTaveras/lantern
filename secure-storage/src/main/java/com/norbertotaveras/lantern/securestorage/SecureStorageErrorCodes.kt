package com.norbertotaveras.lantern.securestorage

/**
 * Stable error codes returned by secure storage APIs.
 */
object SecureStorageErrorCodes {
    /**
     * The provided [SecureStorageKey] value failed validation.
     */
    const val INVALID_KEY = "secure_storage_invalid_key"
    /**
     * A read operation failed.
     */
    const val READ_FAILED = "secure_storage_read_failed"
    /**
     * A write operation failed.
     */
    const val WRITE_FAILED = "secure_storage_write_failed"
    /**
     * A remove operation failed.
     */
    const val REMOVE_FAILED = "secure_storage_remove_failed"
    /**
     * A clear operation failed.
     */
    const val CLEAR_FAILED = "secure_storage_clear_failed"
    /**
     * Fallback code for unexpected secure storage failures.
     */
    const val UNKNOWN = "secure_storage_unknown"
}
