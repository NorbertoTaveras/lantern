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
