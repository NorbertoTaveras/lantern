package com.norbertotaveras.mobilefoundation.mediapicker.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.mediapicker.MediaPickerErrorCodes

internal object MediaMimeTypeValidator {
    private val mimeTypePattern = Regex("^[A-Za-z0-9][A-Za-z0-9!#$&^_.+-]*/[A-Za-z0-9][A-Za-z0-9!#$&^_.+-]*$")

    fun validate(value: String): SdkResult<String> {
        val normalized = value.trim().lowercase()
        if (mimeTypePattern.matches(normalized)) {
            return SdkResult.Success(normalized)
        }

        return SdkResult.Failure(
            SdkError(
                code = MediaPickerErrorCodes.INVALID_MIME_TYPE,
                message = "Media MIME type must use a valid type/subtype format."
            )
        )
    }
}
