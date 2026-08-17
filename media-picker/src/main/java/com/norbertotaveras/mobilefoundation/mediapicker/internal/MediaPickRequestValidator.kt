package com.norbertotaveras.mobilefoundation.mediapicker.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.mediapicker.MediaMimeType
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickerErrorCodes
import com.norbertotaveras.mobilefoundation.mediapicker.MediaSelectionMode
import com.norbertotaveras.mobilefoundation.mediapicker.MediaType

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

        val incompatibleMimeType = request.mimeTypes.firstOrNull { mimeType ->
            !mimeType.isSupportedBy(request.mediaTypes)
        }
        if (incompatibleMimeType != null) {
            return failure(
                "MIME type ${incompatibleMimeType.value} must match one of the requested media types."
            )
        }

        return SdkResult.Success(request)
    }

    private fun MediaMimeType.isSupportedBy(
        mediaTypes: Set<MediaType>
    ): Boolean {
        val type = value.substringBefore("/")
        return when (type) {
            "image" -> MediaType.Image in mediaTypes
            "video" -> MediaType.Video in mediaTypes
            else -> false
        }
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
