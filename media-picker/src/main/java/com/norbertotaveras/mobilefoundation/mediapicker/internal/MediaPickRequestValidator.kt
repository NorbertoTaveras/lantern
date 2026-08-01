package com.norbertotaveras.mobilefoundation.mediapicker.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickerErrorCodes
import com.norbertotaveras.mobilefoundation.mediapicker.MediaSelectionMode

internal object MediaPickRequestValidator {
    fun validate(request: MediaPickRequest): SdkResult<MediaPickRequest> {
        if (request.mediaTypes.isEmpty()) {
            return failure("At least one media type must be requested.")
        }

        if (request.maxItems < 1) {
            return failure("maxItems must be at least 1.")
        }

        if (request.selectionMode == MediaSelectionMode.Single && request.maxItems != 1) {
            return failure("Single selection requests must use maxItems = 1.")
        }

        return SdkResult.Success(request)
    }

    private fun failure(message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = MediaPickerErrorCodes.INVALID_REQUEST,
                message = message
            )
        )
    }
}
