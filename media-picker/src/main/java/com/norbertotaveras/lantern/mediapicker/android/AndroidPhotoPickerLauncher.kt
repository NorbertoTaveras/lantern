package com.norbertotaveras.lantern.mediapicker.android

import com.norbertotaveras.lantern.mediapicker.MediaPickerResult

/**
 * App-provided launcher bridge for Android Photo Picker activity result APIs.
 */
fun interface AndroidPhotoPickerLauncher {
    /**
     * Launches the picker for [request] and suspends until a result is available.
     */
    suspend fun launch(request: AndroidPhotoPickerRequest): MediaPickerResult
}
