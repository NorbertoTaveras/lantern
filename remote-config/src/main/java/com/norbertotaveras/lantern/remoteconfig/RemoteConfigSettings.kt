package com.norbertotaveras.lantern.remoteconfig

/**
 * Provider settings for remote config fetch behavior.
 */
data class RemoteConfigSettings(
    /**
     * Minimum time between provider fetches in milliseconds.
     */
    val minimumFetchIntervalMillis: Long = DEFAULT_MINIMUM_FETCH_INTERVAL_MILLIS,
    /**
     * Maximum time to wait for a provider fetch in milliseconds.
     */
    val fetchTimeoutMillis: Long = DEFAULT_FETCH_TIMEOUT_MILLIS
) {
    init {
        require(minimumFetchIntervalMillis >= 0) {
            "minimumFetchIntervalMillis must be greater than or equal to 0."
        }
        require(fetchTimeoutMillis >= 0) {
            "fetchTimeoutMillis must be greater than or equal to 0."
        }
    }

    companion object {
        /**
         * Default minimum fetch interval of one hour.
         */
        const val DEFAULT_MINIMUM_FETCH_INTERVAL_MILLIS = 3_600_000L
        /**
         * Default fetch timeout of one minute.
         */
        const val DEFAULT_FETCH_TIMEOUT_MILLIS = 60_000L
    }
}
