package com.norbertotaveras.mobilefoundation.mediapicker

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.mediapicker.internal.MediaMimeTypeValidator

@JvmInline
value class MediaMimeType private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<MediaMimeType> {
            return when (val result = MediaMimeTypeValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(MediaMimeType(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): MediaMimeType {
            return MediaMimeType(value)
        }
    }
}
