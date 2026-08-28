/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.network.okhttp

import com.norbertotaveras.lantern.core.SdkError
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import okhttp3.Response

/**
 * Maps common networking failures into SDK-level errors that app layers can handle consistently.
 */
class NetworkErrorMapper {

    /**
     * Maps a thrown network failure into an [SdkError].
     */
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
            message = throwable.localizedMessage.takeUnless { it.isNullOrBlank() } ?: "Network request failed.",
            cause = throwable
        )
    }

    /**
     * Maps an HTTP response failure into an [SdkError] with status and URL metadata.
     */
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

    /**
     * Creates an error for exhausted retry attempts.
     */
    fun retryExhausted(attempts: Int, cause: Throwable? = null): SdkError {
        return SdkError(
            code = NetworkErrorCodes.RETRY_EXHAUSTED,
            message = "Network retry attempts exhausted.",
            cause = cause,
            metadata = mapOf("attempts" to attempts.toString())
        )
    }
}
