package com.norbertotaveras.mobilefoundation.backgroundwork

sealed interface BackgroundWorkType {
    data object OneTime : BackgroundWorkType

    data class Periodic(
        val repeatIntervalMillis: Long,
        val flexIntervalMillis: Long? = null
    ) : BackgroundWorkType
}
