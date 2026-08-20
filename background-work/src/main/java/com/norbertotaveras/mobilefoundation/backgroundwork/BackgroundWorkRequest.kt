package com.norbertotaveras.mobilefoundation.backgroundwork

/**
 * Provider-neutral request to enqueue background work.
 */
data class BackgroundWorkRequest(
    val name: BackgroundWorkName,
    val type: BackgroundWorkType,
    val policy: BackgroundWorkPolicy = BackgroundWorkPolicy.KeepExisting,
    val constraints: BackgroundWorkConstraints = BackgroundWorkConstraints.None,
    val input: Map<String, String> = emptyMap(),
    val initialDelayMillis: Long = 0L
)
