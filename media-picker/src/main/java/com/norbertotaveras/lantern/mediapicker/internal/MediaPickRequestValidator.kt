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

package com.norbertotaveras.lantern.mediapicker.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.MediaMimeType
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickerErrorCodes
import com.norbertotaveras.lantern.mediapicker.MediaSelectionMode
import com.norbertotaveras.lantern.mediapicker.MediaType

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
