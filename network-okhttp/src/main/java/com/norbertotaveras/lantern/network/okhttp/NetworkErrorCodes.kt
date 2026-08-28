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

/**
 * Stable error codes returned by network helpers.
 */
object NetworkErrorCodes {
    /**
     * Fallback code for unexpected network failures.
     */
    const val UNKNOWN = "network_unknown"
    /**
     * The network helper was configured with invalid values.
     */
    const val INVALID_CONFIGURATION = "network_invalid_configuration"
    /**
     * A request failed before receiving an HTTP response.
     */
    const val REQUEST_FAILED = "network_request_failed"
    /**
     * A request completed with a non-success HTTP response.
     */
    const val HTTP_ERROR = "network_http_error"
    /**
     * A request timed out.
     */
    const val TIMEOUT = "network_timeout"
    /**
     * The device or host could not establish a network connection.
     */
    const val NO_CONNECTION = "network_no_connection"
    /**
     * Retry attempts were exhausted.
     */
    const val RETRY_EXHAUSTED = "network_retry_exhausted"
}
