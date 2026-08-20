package com.norbertotaveras.mobilefoundation.backgroundwork

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Identifier for enqueued background work.
 */
@JvmInline
value class BackgroundWorkId private constructor(val value: String) {
    companion object {
        /**
         * Creates a [BackgroundWorkId] after validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<BackgroundWorkId> {
            return if (value.isBlank()) {
                SdkResult.Failure(
                    SdkError(
                        code = BackgroundWorkErrorCodes.INVALID_WORK_ID,
                        message = "Background work id cannot be blank."
                    )
                )
            } else {
                SdkResult.Success(BackgroundWorkId(value))
            }
        }

        /**
         * Creates a [BackgroundWorkId] without validation for trusted constants.
         */
        fun unsafe(value: String): BackgroundWorkId {
            return BackgroundWorkId(value)
        }
    }
}
