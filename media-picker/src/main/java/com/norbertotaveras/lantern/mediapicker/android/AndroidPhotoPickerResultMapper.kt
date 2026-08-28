/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.mediapicker.android

import android.net.Uri
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickerResult
import com.norbertotaveras.lantern.mediapicker.MediaType
import com.norbertotaveras.lantern.mediapicker.PickedMediaItem

/**
 * Maps Android Photo Picker URI results into provider-neutral picker results.
 */
class AndroidPhotoPickerResultMapper {

    /**
     * Maps a single picker [uri], returning [MediaPickerResult.Cancelled] when it is `null`.
     */
    fun mapSingle(
        uri: Uri?,
        request: MediaPickRequest
    ): MediaPickerResult {
        return uri?.let { mapUris(listOf(it), request) } ?: MediaPickerResult.Cancelled
    }

    /**
     * Maps multiple picker [uris], returning [MediaPickerResult.Cancelled] when the list is empty.
     */
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
