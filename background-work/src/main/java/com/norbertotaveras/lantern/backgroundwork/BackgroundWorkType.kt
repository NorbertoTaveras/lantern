package com.norbertotaveras.lantern.backgroundwork

/**
 * Scheduling type for a background work request.
 */
sealed interface BackgroundWorkType {
    /**
     * Runs once.
     */
    data object OneTime : BackgroundWorkType

    /**
     * Runs repeatedly using [repeatIntervalMillis] and optional [flexIntervalMillis].
     */
    data class Periodic(
        val repeatIntervalMillis: Long,
        val flexIntervalMillis: Long? = null
    ) : BackgroundWorkType
}
