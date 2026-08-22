package com.norbertotaveras.lantern.mediapicker

import com.norbertotaveras.lantern.core.SdkResult

/**
 * Provider-neutral media picker contract.
 */
interface MediaPicker {
    /**
     * Launches media selection for [request].
     */
    suspend fun pick(request: MediaPickRequest): SdkResult<MediaPickerResult>
}
