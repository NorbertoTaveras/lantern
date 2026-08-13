package com.norbertotaveras.mobilefoundation.backgroundwork

data class BackgroundWorkConstraints(
    val requiresNetwork: Boolean = false,
    val requiresCharging: Boolean = false,
    val requiresBatteryNotLow: Boolean = false,
    val requiresStorageNotLow: Boolean = false
) {
    companion object {
        val None = BackgroundWorkConstraints()
    }
}
