package com.norbertotaveras.mobilefoundation.network.okhttp

/**
 * Retry policy for [RetryInterceptor].
 *
 * Defaults are conservative and only retry idempotent requests for transient status codes or IO failures.
 */
data class NetworkRetryConfig(
    val maxRetries: Int = DEFAULT_MAX_RETRIES,
    val initialDelayMillis: Long = DEFAULT_INITIAL_DELAY_MILLIS,
    val maxDelayMillis: Long = DEFAULT_MAX_DELAY_MILLIS,
    val backoffMultiplier: Double = DEFAULT_BACKOFF_MULTIPLIER,
    val retryStatusCodes: Set<Int> = DEFAULT_RETRY_STATUS_CODES
) {
    init {
        require(maxRetries >= 0) { "maxRetries must be greater than or equal to 0." }
        require(initialDelayMillis >= 0) { "initialDelayMillis must be greater than or equal to 0." }
        require(maxDelayMillis >= initialDelayMillis) { "maxDelayMillis must be greater than or equal to initialDelayMillis." }
        require(backoffMultiplier >= 1.0) { "backoffMultiplier must be greater than or equal to 1.0." }
    }

    fun delayForRetry(retryNumber: Int): Long {
        require(retryNumber >= 1) { "retryNumber must be greater than or equal to 1." }

        val delay = initialDelayMillis * Math.pow(backoffMultiplier, (retryNumber - 1).toDouble())
        return delay.toLong().coerceAtMost(maxDelayMillis)
    }

    companion object {
        const val DEFAULT_MAX_RETRIES = 2
        const val DEFAULT_INITIAL_DELAY_MILLIS = 250L
        const val DEFAULT_MAX_DELAY_MILLIS = 2_000L
        const val DEFAULT_BACKOFF_MULTIPLIER = 2.0
        val DEFAULT_RETRY_STATUS_CODES = setOf(408, 429, 500, 502, 503, 504)
    }
}
