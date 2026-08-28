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

package com.norbertotaveras.lantern.mediapicker.android.internal

import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaSelectionMode
import com.norbertotaveras.lantern.mediapicker.MediaType
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerContractType
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerRequest

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
