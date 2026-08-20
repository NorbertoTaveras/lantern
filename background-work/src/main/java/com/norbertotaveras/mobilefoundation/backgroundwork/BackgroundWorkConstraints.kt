package com.norbertotaveras.mobilefoundation.backgroundwork

/**
 * Constraints required before background work can run.
 */
data class BackgroundWorkConstraints(
    val requiresNetwork: Boolean = false,
    val requiresCharging: Boolean = false,
    val requiresBatteryNotLow: Boolean = false,
    val requiresStorageNotLow: Boolean = false
) {
    companion object {
        /**
         * No runtime constraints.
         */
        val None = BackgroundWorkConstraints()
    }
}
