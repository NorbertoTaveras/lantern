package com.norbertotaveras.mobilefoundation.backgroundwork

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult

@JvmInline
value class BackgroundWorkName private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<BackgroundWorkName> {
            return if (value.isBlank()) {
                SdkResult.Failure(
                    SdkError(
                        code = BackgroundWorkErrorCodes.INVALID_WORK_NAME,
                        message = "Background work name cannot be blank."
                    )
                )
            } else {
                SdkResult.Success(BackgroundWorkName(value))
            }
        }

        fun unsafe(value: String): BackgroundWorkName {
            return BackgroundWorkName(value)
        }
    }
}
