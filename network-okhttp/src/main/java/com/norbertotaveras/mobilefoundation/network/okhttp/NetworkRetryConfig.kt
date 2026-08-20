package com.norbertotaveras.mobilefoundation.network.okhttp

/**
 * Retry policy for [RetryInterceptor].
 *
 * Defaults are conservative and only retry idempotent requests for transient status codes or IO failures.
 */
data class NetworkRetryConfig(
    /**
     * Maximum number of retry attempts after the initial request.
     */
    val maxRetries: Int = DEFAULT_MAX_RETRIES,
    /**
     * Delay for the first retry attempt in milliseconds.
     */
    val initialDelayMillis: Long = DEFAULT_INITIAL_DELAY_MILLIS,
    /**
     * Upper bound for computed retry delays in milliseconds.
     */
    val maxDelayMillis: Long = DEFAULT_MAX_DELAY_MILLIS,
    /**
     * Exponential backoff multiplier applied after the first retry.
     */
    val backoffMultiplier: Double = DEFAULT_BACKOFF_MULTIPLIER,
    /**
     * HTTP status codes that should be retried for idempotent request methods.
     */
    val retryStatusCodes: Set<Int> = DEFAULT_RETRY_STATUS_CODES
) {
    init {
        require(maxRetries >= 0) { "maxRetries must be greater than or equal to 0." }
        require(initialDelayMillis >= 0) { "initialDelayMillis must be greater than or equal to 0." }
        require(maxDelayMillis >= initialDelayMillis) { "maxDelayMillis must be greater than or equal to initialDelayMillis." }
        require(backoffMultiplier >= 1.0) { "backoffMultiplier must be greater than or equal to 1.0." }
        require(retryStatusCodes.all { it in HTTP_STATUS_CODE_RANGE }) {
            "retryStatusCodes can only contain valid HTTP status codes."
        }
    }

    /**
     * Returns the delay for a one-based retry number.
     */
    fun delayForRetry(retryNumber: Int): Long {
        require(retryNumber >= 1) { "retryNumber must be greater than or equal to 1." }

        if (initialDelayMillis == 0L) {
            return 0L
        }

        val delay = initialDelayMillis.toDouble() * Math.pow(backoffMultiplier, (retryNumber - 1).toDouble())
        if (delay.isInfinite() || delay.isNaN() || delay >= maxDelayMillis.toDouble()) {
            return maxDelayMillis
        }

        return delay.toLong()
    }

    companion object {
        /**
         * Default number of retry attempts after the initial request.
         */
        const val DEFAULT_MAX_RETRIES = 2
        /**
         * Default delay for the first retry attempt.
         */
        const val DEFAULT_INITIAL_DELAY_MILLIS = 250L
        /**
         * Default maximum delay between retry attempts.
         */
        const val DEFAULT_MAX_DELAY_MILLIS = 2_000L
        /**
         * Default exponential backoff multiplier.
         */
        const val DEFAULT_BACKOFF_MULTIPLIER = 2.0
        /**
         * Default transient HTTP statuses retried for idempotent request methods.
         */
        val DEFAULT_RETRY_STATUS_CODES = setOf(408, 429, 500, 502, 503, 504)

        private val HTTP_STATUS_CODE_RANGE = 100..599
    }
}
