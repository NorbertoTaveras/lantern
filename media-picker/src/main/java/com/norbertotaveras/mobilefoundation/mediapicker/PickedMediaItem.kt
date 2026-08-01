package com.norbertotaveras.mobilefoundation.mediapicker

data class PickedMediaItem(
    val uri: String,
    val mediaType: MediaType,
    val mimeType: MediaMimeType? = null,
    val displayName: String? = null,
    val sizeBytes: Long? = null,
    val metadata: Map<String, String> = emptyMap()
)
