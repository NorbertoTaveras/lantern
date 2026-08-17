package com.norbertotaveras.mobilefoundation.network.okhttp

/**
 * Base configuration used when creating Mobile Foundation OkHttp clients.
 *
 * Timeout values are in milliseconds. A timeout value of 0 uses OkHttp's no-timeout behavior.
 */
data class NetworkConfig(
    val connectTimeoutMillis: Long = DEFAULT_CONNECT_TIMEOUT_MILLIS,
    val readTimeoutMillis: Long = DEFAULT_READ_TIMEOUT_MILLIS,
    val writeTimeoutMillis: Long = DEFAULT_WRITE_TIMEOUT_MILLIS,
    val callTimeoutMillis: Long = DEFAULT_CALL_TIMEOUT_MILLIS,
    val defaultHeaders: Map<String, String> = emptyMap(),
    val followRedirects: Boolean = true,
    val followSslRedirects: Boolean = true,
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
    }

    companion object {
        const val DEFAULT_CONNECT_TIMEOUT_MILLIS = 10_000L
        const val DEFAULT_READ_TIMEOUT_MILLIS = 30_000L
        const val DEFAULT_WRITE_TIMEOUT_MILLIS = 30_000L
        const val DEFAULT_CALL_TIMEOUT_MILLIS = 0L

        private val headerNamePattern = Regex("^[!#$%&'*+.^_`|~0-9A-Za-z-]+$")
    }

    private fun String.isValidHeaderName(): Boolean {
        return headerNamePattern.matches(this)
    }
}
