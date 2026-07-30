package com.norbertotaveras.mobilefoundation.remoteconfig

data class RemoteConfigSettings(
    val minimumFetchIntervalMillis: Long = DEFAULT_MINIMUM_FETCH_INTERVAL_MILLIS,
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
        const val DEFAULT_MINIMUM_FETCH_INTERVAL_MILLIS = 3_600_000L
        const val DEFAULT_FETCH_TIMEOUT_MILLIS = 60_000L
    }
}
