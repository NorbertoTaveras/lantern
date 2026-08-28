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

package com.norbertotaveras.lantern.mediapicker

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.internal.MediaMimeTypeValidator

/**
 * Validated MIME type used to narrow media picker results.
 */
@JvmInline
value class MediaMimeType private constructor(val value: String) {
    companion object {
        /**
         * Creates a [MediaMimeType] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<MediaMimeType> {
            return when (val result = MediaMimeTypeValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(MediaMimeType(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [MediaMimeType] without validation for trusted constants.
         */
        fun unsafe(value: String): MediaMimeType {
            return MediaMimeType(value)
        }
    }
}
