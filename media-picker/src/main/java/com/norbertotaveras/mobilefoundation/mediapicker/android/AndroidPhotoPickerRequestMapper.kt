package com.norbertotaveras.mobilefoundation.mediapicker.android

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest
import com.norbertotaveras.mobilefoundation.mediapicker.MediaSelectionMode
import com.norbertotaveras.mobilefoundation.mediapicker.MediaType

internal class AndroidPhotoPickerRequestMapper {

    fun map(request: MediaPickRequest): AndroidPhotoPickerRequest {
        return AndroidPhotoPickerRequest(
            sourceRequest = request,
            contractType = request.toContractType(),
            visualMediaRequest = PickVisualMediaRequest.Builder()
                .setMediaType(request.toVisualMediaType())
                .build(),
            maxItems = request.maxItems
        )
    }

    private fun MediaPickRequest.toContractType(): AndroidPhotoPickerContractType {
        return when (selectionMode) {
            MediaSelectionMode.Single -> AndroidPhotoPickerContractType.Single
            MediaSelectionMode.Multiple -> AndroidPhotoPickerContractType.Multiple
        }
    }

    private fun MediaPickRequest.toVisualMediaType(): ActivityResultContracts.PickVisualMedia.VisualMediaType {
        val singleMimeType = mimeTypes.singleOrNull()
        if (singleMimeType != null) {
            return ActivityResultContracts.PickVisualMedia.SingleMimeType(singleMimeType.value)
        }

        return when (mediaTypes) {
            setOf(MediaType.Image) -> ActivityResultContracts.PickVisualMedia.ImageOnly
            setOf(MediaType.Video) -> ActivityResultContracts.PickVisualMedia.VideoOnly
            else -> ActivityResultContracts.PickVisualMedia.ImageAndVideo
        }
    }
}
