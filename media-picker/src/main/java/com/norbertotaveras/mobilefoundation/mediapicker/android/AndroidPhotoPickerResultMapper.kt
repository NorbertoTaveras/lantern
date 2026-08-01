package com.norbertotaveras.mobilefoundation.mediapicker.android

import android.net.Uri
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickRequest
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickerResult
import com.norbertotaveras.mobilefoundation.mediapicker.MediaType
import com.norbertotaveras.mobilefoundation.mediapicker.PickedMediaItem

class AndroidPhotoPickerResultMapper {

    fun mapSingle(
        uri: Uri?,
        request: MediaPickRequest
    ): MediaPickerResult {
        return uri?.let { mapUris(listOf(it), request) } ?: MediaPickerResult.Cancelled
    }

    fun mapMultiple(
        uris: List<Uri>,
        request: MediaPickRequest
    ): MediaPickerResult {
        return if (uris.isEmpty()) {
            MediaPickerResult.Cancelled
        } else {
            mapUris(uris, request)
        }
    }

    private fun mapUris(
        uris: List<Uri>,
        request: MediaPickRequest
    ): MediaPickerResult {
        val fallbackMediaType = request.mediaTypes.singleOrNull() ?: MediaType.Image
        return MediaPickerResult(
            items = uris.map { uri ->
                PickedMediaItem(
                    uri = uri.toString(),
                    mediaType = fallbackMediaType,
                    mimeType = request.mimeTypes.singleOrNull()
                )
            }
        )
    }
}
