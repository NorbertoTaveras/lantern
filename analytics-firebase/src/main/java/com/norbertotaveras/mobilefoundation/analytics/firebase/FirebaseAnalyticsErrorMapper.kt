package com.norbertotaveras.mobilefoundation.analytics.firebase

import com.norbertotaveras.mobilefoundation.core.SdkError

/**
 * Maps Firebase Analytics failures into SDK errors.
 *
 * This mapper is exposed for hosts that wrap [FirebaseAnalyticsProvider] and want to keep error
 * codes aligned with the SDK provider implementation.
 */
object FirebaseAnalyticsErrorMapper {
    /**
     * Creates an error for a failed Firebase Analytics event tracking call.
     */
    fun trackFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.TRACK_FAILED,
            message = "Unable to track Firebase Analytics event.",
            cause = cause
        )
    }

    /**
     * Creates an error for a failed Firebase Analytics user ID update.
     */
    fun userIdFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.USER_ID_FAILED,
            message = "Unable to set Firebase Analytics user ID.",
            cause = cause
        )
    }

    /**
     * Creates an error for a failed Firebase Analytics user property update.
     */
    fun userPropertyFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.USER_PROPERTY_FAILED,
            message = "Unable to set Firebase Analytics user property.",
            cause = cause
        )
    }

    /**
     * Creates an error for a failed Firebase Analytics data reset.
     */
    fun resetFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.RESET_FAILED,
            message = "Unable to reset Firebase Analytics data.",
            cause = cause
        )
    }
}
