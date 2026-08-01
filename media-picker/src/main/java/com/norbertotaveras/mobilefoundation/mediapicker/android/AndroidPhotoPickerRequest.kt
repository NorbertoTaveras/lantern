package com.norbertotaveras.mobilefoundation.mediapicker.android

import androidx.activity.result.PickVisualMediaRequest
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest

data class AndroidPhotoPickerRequest(
    val sourceRequest: MediaPickRequest,
    val contractType: AndroidPhotoPickerContractType,
    val visualMediaRequest: PickVisualMediaRequest,
    val maxItems: Int
)
