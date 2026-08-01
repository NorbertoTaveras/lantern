package com.norbertotaveras.mobilefoundation.mediapicker.android

import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickerResult

fun interface AndroidPhotoPickerLauncher {
    suspend fun launch(request: AndroidPhotoPickerRequest): MediaPickerResult
}
