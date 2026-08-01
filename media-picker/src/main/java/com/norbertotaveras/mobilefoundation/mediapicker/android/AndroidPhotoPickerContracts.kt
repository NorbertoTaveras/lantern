package com.norbertotaveras.mobilefoundation.mediapicker.android

import androidx.activity.result.contract.ActivityResultContracts

object AndroidPhotoPickerContracts {
    fun single(): ActivityResultContracts.PickVisualMedia {
        return ActivityResultContracts.PickVisualMedia()
    }

    fun multiple(maxItems: Int): ActivityResultContracts.PickMultipleVisualMedia {
        return ActivityResultContracts.PickMultipleVisualMedia(maxItems)
    }
}
