package com.norbertotaveras.mobilefoundation.mediapicker

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface MediaPicker {
    suspend fun pick(request: MediaPickRequest): SdkResult<MediaPickerResult>
}
