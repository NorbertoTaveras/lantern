package com.norbertotaveras.mobilefoundation.network.okhttp

import com.norbertotaveras.mobilefoundation.core.SdkError
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import okhttp3.Response

/**
 * Maps common networking failures into SDK-level errors that app layers can handle consistently.
 */
class NetworkErrorMapper {

    fun map(throwable: Throwable): SdkError {
        val code = when (throwable) {
            is SocketTimeoutException -> NetworkErrorCodes.TIMEOUT
            is UnknownHostException,
            is ConnectException -> NetworkErrorCodes.NO_CONNECTION
            is IOException -> NetworkErrorCodes.REQUEST_FAILED
            else -> NetworkErrorCodes.UNKNOWN
        }

        return SdkError(
            code = code,
            message = throwable.localizedMessage ?: "Network request failed.",
            cause = throwable
        )
    }

    fun mapHttp(response: Response): SdkError {
        return SdkError(
            code = NetworkErrorCodes.HTTP_ERROR,
            message = "Network request failed with HTTP ${response.code}.",
            metadata = mapOf(
                "http_status" to response.code.toString(),
                "url" to response.request.url.toString()
            )
        )
    }

    fun retryExhausted(attempts: Int, cause: Throwable? = null): SdkError {
        return SdkError(
            code = NetworkErrorCodes.RETRY_EXHAUSTED,
            message = "Network retry attempts exhausted.",
            cause = cause,
            metadata = mapOf("attempts" to attempts.toString())
        )
    }
}
