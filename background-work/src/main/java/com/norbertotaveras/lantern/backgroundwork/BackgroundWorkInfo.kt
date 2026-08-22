package com.norbertotaveras.lantern.backgroundwork

/**
 * Current state and data for scheduled background work.
 */
data class BackgroundWorkInfo(
    val id: BackgroundWorkId,
    val name: BackgroundWorkName,
    val status: BackgroundWorkStatus,
    val progress: Map<String, String> = emptyMap(),
    val output: Map<String, String> = emptyMap()
)
