package com.norbertotaveras.lantern.mediapicker

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.internal.MediaPickRequestValidator

/**
 * Request describing what media the picker should return.
 */
data class MediaPickRequest(
    /**
     * Media categories accepted by the picker.
     */
    val mediaTypes: Set<MediaType> = setOf(MediaType.Image),
    /**
     * Whether the caller expects one item or multiple items.
     */
    val selectionMode: MediaSelectionMode = MediaSelectionMode.Single,
    /**
     * Maximum number of items for multiple selection.
     */
    val maxItems: Int = 1,
    /**
     * Optional MIME type filters. A single MIME type maps to Android Photo Picker MIME filtering.
     */
    val mimeTypes: Set<MediaMimeType> = emptySet(),
    /**
     * Whether the caller wants persistable URI permission when supported by an implementation.
     */
    val requirePersistablePermission: Boolean = false
) {
    /**
     * Validates request constraints before launch.
     */
    fun validate(): SdkResult<MediaPickRequest> {
        return MediaPickRequestValidator.validate(this)
    }
}
