package com.norbertotaveras.mobilefoundation.backgroundwork

data class BackgroundWorkInfo(
    val id: BackgroundWorkId,
    val name: BackgroundWorkName,
    val status: BackgroundWorkStatus,
    val progress: Map<String, String> = emptyMap(),
    val output: Map<String, String> = emptyMap()
)
