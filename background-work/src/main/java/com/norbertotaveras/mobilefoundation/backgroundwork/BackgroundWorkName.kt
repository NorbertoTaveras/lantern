package com.norbertotaveras.mobilefoundation.backgroundwork

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Unique logical name for background work.
 */
@JvmInline
value class BackgroundWorkName private constructor(val value: String) {
    companion object {
        /**
         * Creates a [BackgroundWorkName] after validating [value].
         */
        @JvmStatic
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

        /**
         * Creates a [BackgroundWorkName] without validation for trusted constants.
         */
        fun unsafe(value: String): BackgroundWorkName {
            return BackgroundWorkName(value)
        }
    }
}
