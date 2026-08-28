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

import java.util.Locale

/**
 * Base configuration used when creating Lantern OkHttp clients.
 *
 * Timeout values are in milliseconds. A timeout value of 0 uses OkHttp's no-timeout behavior.
 */
data class NetworkConfig(
    /**
     * TCP connection timeout in milliseconds.
     */
    val connectTimeoutMillis: Long = DEFAULT_CONNECT_TIMEOUT_MILLIS,
    /**
     * Response body read timeout in milliseconds.
     */
    val readTimeoutMillis: Long = DEFAULT_READ_TIMEOUT_MILLIS,
    /**
     * Request body write timeout in milliseconds.
     */
    val writeTimeoutMillis: Long = DEFAULT_WRITE_TIMEOUT_MILLIS,
    /**
     * Full-call timeout in milliseconds. The default of 0 disables the call timeout.
     */
    val callTimeoutMillis: Long = DEFAULT_CALL_TIMEOUT_MILLIS,
    /**
     * Headers applied to every request unless another interceptor replaces them later.
     */
    val defaultHeaders: Map<String, String> = emptyMap(),
    /**
     * Whether OkHttp follows HTTP redirects.
     */
    val followRedirects: Boolean = true,
    /**
     * Whether OkHttp follows redirects between HTTP and HTTPS.
     */
    val followSslRedirects: Boolean = true,
    /**
     * Whether OkHttp retries connection-level failures.
     */
    val retryOnConnectionFailure: Boolean = true
) {
    init {
        require(connectTimeoutMillis >= 0) { "connectTimeoutMillis must be greater than or equal to 0." }
        require(readTimeoutMillis >= 0) { "readTimeoutMillis must be greater than or equal to 0." }
        require(writeTimeoutMillis >= 0) { "writeTimeoutMillis must be greater than or equal to 0." }
        require(callTimeoutMillis >= 0) { "callTimeoutMillis must be greater than or equal to 0." }
        require(defaultHeaders.keys.none { it.isBlank() }) { "defaultHeaders cannot contain blank header names." }
        require(defaultHeaders.keys.all { it.isValidHeaderName() }) {
            "defaultHeaders can only contain valid HTTP header names."
        }
        require(defaultHeaders.keys.distinctBy { it.lowercase(Locale.US) }.size == defaultHeaders.size) {
            "defaultHeaders cannot contain duplicate header names."
        }
        require(defaultHeaders.values.all { it.isValidHeaderValue() }) {
            "defaultHeaders cannot contain invalid HTTP header values."
        }
    }

    companion object {
        /**
         * Default TCP connection timeout.
         */
        const val DEFAULT_CONNECT_TIMEOUT_MILLIS = 10_000L
        /**
         * Default response body read timeout.
         */
        const val DEFAULT_READ_TIMEOUT_MILLIS = 30_000L
        /**
         * Default request body write timeout.
         */
        const val DEFAULT_WRITE_TIMEOUT_MILLIS = 30_000L
        /**
         * Default full-call timeout. A value of 0 means no timeout.
         */
        const val DEFAULT_CALL_TIMEOUT_MILLIS = 0L

        private val headerNamePattern = Regex("^[!#$%&'*+.^_`|~0-9A-Za-z-]+$")
    }

    private fun String.isValidHeaderName(): Boolean {
        return headerNamePattern.matches(this)
    }

    private fun String.isValidHeaderValue(): Boolean {
        return all { character ->
            character == '\t' || !character.isISOControl()
        }
    }
}
