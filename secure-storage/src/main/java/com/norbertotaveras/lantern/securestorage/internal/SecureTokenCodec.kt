package com.norbertotaveras.lantern.securestorage.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.securestorage.SecureStorageErrorCodes
import com.norbertotaveras.lantern.securestorage.SecureToken
import com.norbertotaveras.lantern.securestorage.SecureTokenType
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

internal class SecureTokenCodec(
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
