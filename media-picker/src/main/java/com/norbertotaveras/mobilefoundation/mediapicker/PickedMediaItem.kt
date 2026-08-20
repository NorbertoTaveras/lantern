package com.norbertotaveras.mobilefoundation.mediapicker

/**
 * Normalized media item selected by a picker.
 */
data class PickedMediaItem(
    /**
     * String representation of the selected item URI.
     */
    val uri: String,
    /**
     * Best-known media category for the item.
     */
    val mediaType: MediaType,
    /**
     * Best-known MIME type for the item, when available.
     */
    val mimeType: MediaMimeType? = null,
    /**
     * Display name for the selected item, when available.
     */
    val displayName: String? = null,
    /**
     * File size in bytes, when available.
     */
    val sizeBytes: Long? = null,
    /**
     * Optional lightweight platform or provider metadata.
     */
    val metadata: Map<String, String> = emptyMap()
)
