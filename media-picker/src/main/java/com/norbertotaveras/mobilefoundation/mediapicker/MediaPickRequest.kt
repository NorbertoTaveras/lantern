package com.norbertotaveras.mobilefoundation.mediapicker

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.mediapicker.internal.MediaPickRequestValidator

data class MediaPickRequest(
    val mediaTypes: Set<MediaType> = setOf(MediaType.Image),
    val selectionMode: MediaSelectionMode = MediaSelectionMode.Single,
    val maxItems: Int = 1,
    val mimeTypes: Set<MediaMimeType> = emptySet(),
    val requirePersistablePermission: Boolean = false
) {
    fun validate(): SdkResult<MediaPickRequest> {
        return MediaPickRequestValidator.validate(this)
    }
}
