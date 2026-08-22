package com.norbertotaveras.lantern.mediapicker.android

import androidx.activity.result.contract.ActivityResultContracts

/**
 * Factory for AndroidX Photo Picker activity result contracts.
 */
object AndroidPhotoPickerContracts {
    /**
     * Creates a single-item visual media picker contract.
     */
    fun single(): ActivityResultContracts.PickVisualMedia {
        return ActivityResultContracts.PickVisualMedia()
    }

    /**
     * Creates a multiple-item visual media picker contract constrained to [maxItems].
     */
    fun multiple(maxItems: Int): ActivityResultContracts.PickMultipleVisualMedia {
        return ActivityResultContracts.PickMultipleVisualMedia(maxItems)
    }
}
