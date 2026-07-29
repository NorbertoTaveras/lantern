package com.norbertotaveras.mobilefoundation.securestorage.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.securestorage.SecureStorageErrorCodes
import com.norbertotaveras.mobilefoundation.securestorage.SecureToken
import com.norbertotaveras.mobilefoundation.securestorage.SecureTokenType
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

class SecureTokenCodec(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
    }
) {

    fun encode(token: SecureToken): SdkResult<String> {
        return try {
            SdkResult.Success(json.encodeToString(token.toPayload()))
        } catch (throwable: SerializationException) {
            SdkResult.Failure(
                SdkError(
                    code = SecureStorageErrorCodes.WRITE_FAILED,
                    message = throwable.localizedMessage ?: "Unable to encode secure token.",
                    cause = throwable
                )
            )
        }
    }

    fun decode(value: String): SdkResult<SecureToken> {
        return try {
            SdkResult.Success(json.decodeFromString<SecureTokenPayload>(value).toToken())
        } catch (throwable: SerializationException) {
            SdkResult.Failure(
                SdkError(
                    code = SecureStorageErrorCodes.READ_FAILED,
                    message = throwable.localizedMessage ?: "Unable to decode secure token.",
                    cause = throwable
                )
            )
        } catch (throwable: IllegalArgumentException) {
            SdkResult.Failure(
                SdkError(
                    code = SecureStorageErrorCodes.READ_FAILED,
                    message = throwable.localizedMessage ?: "Unable to decode secure token.",
                    cause = throwable
                )
            )
        }
    }

    private fun SecureToken.toPayload(): SecureTokenPayload {
        return SecureTokenPayload(
            value = value,
            type = type.name,
            expiresAtEpochMillis = expiresAtEpochMillis,
            metadata = metadata
        )
    }

    private fun SecureTokenPayload.toToken(): SecureToken {
        return SecureToken(
            value = value,
            type = SecureTokenType.valueOf(type),
            expiresAtEpochMillis = expiresAtEpochMillis,
            metadata = metadata
        )
    }
}

@Serializable
internal data class SecureTokenPayload(
    val value: String,
    val type: String,
    val expiresAtEpochMillis: Long? = null,
    val metadata: Map<String, String> = emptyMap()
)
