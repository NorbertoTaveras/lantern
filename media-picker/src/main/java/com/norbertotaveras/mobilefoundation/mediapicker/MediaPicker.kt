package com.norbertotaveras.mobilefoundation.mediapicker

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Provider-neutral media picker contract.
 */
interface MediaPicker {
    /**
     * Launches media selection for [request].
     */
    suspend fun pick(request: MediaPickRequest): SdkResult<MediaPickerResult>
}
