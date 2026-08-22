package com.norbertotaveras.lantern.mediapicker.android

import androidx.activity.result.PickVisualMediaRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest

/**
 * Android Photo Picker request derived from a provider-neutral [MediaPickRequest].
 */
data class AndroidPhotoPickerRequest(
    /**
     * Original provider-neutral request.
     */
    val sourceRequest: MediaPickRequest,
    /**
     * Activity Result contract type required for the request.
     */
    val contractType: AndroidPhotoPickerContractType,
    /**
     * AndroidX visual media request.
     */
    val visualMediaRequest: PickVisualMediaRequest,
    /**
     * Maximum number of items requested.
     */
    val maxItems: Int
)
