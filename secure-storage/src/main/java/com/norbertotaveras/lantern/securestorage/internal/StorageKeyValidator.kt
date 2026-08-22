package com.norbertotaveras.lantern.securestorage.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.SecureStorageErrorCodes

internal object StorageKeyValidator {
    private const val MAX_KEY_LENGTH = 128
    private val keyPattern = Regex("^[A-Za-z0-9._:-]+$")

    fun validate(key: String): SdkResult<String> {
        val normalizedKey = key.trim()
        return when {
            normalizedKey.isEmpty() -> invalid("Storage key cannot be blank.")
            normalizedKey.length > MAX_KEY_LENGTH -> invalid("Storage key cannot exceed $MAX_KEY_LENGTH characters.")
            !keyPattern.matches(normalizedKey) -> invalid(
                "Storage key can only contain letters, numbers, periods, underscores, colons, and hyphens."
            )
            else -> SdkResult.Success(normalizedKey)
        }
    }

    private fun invalid(message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = SecureStorageErrorCodes.INVALID_KEY,
                message = message
            )
        )
    }
}
