package com.norbertotaveras.lantern.network.okhttp

/**
 * Controls how much metadata [NetworkLoggingInterceptor] emits.
 */
enum class NetworkLoggingLevel {
    /**
     * Disable SDK network logging.
     */
    None,
    /**
     * Log request and response lines only.
     */
    Basic,
    /**
     * Log request and response lines plus headers, with sensitive headers redacted.
     */
    Headers
}
