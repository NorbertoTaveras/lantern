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
