package com.norbertotaveras.mobilefoundation.analytics.firebase

import com.norbertotaveras.mobilefoundation.core.SdkError

/**
 * Maps Firebase Analytics failures into SDK errors.
 */
object FirebaseAnalyticsErrorMapper {
    fun trackFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.TRACK_FAILED,
            message = "Unable to track Firebase Analytics event.",
            cause = cause
        )
    }

    fun userIdFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.USER_ID_FAILED,
            message = "Unable to set Firebase Analytics user ID.",
            cause = cause
        )
    }

    fun userPropertyFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.USER_PROPERTY_FAILED,
            message = "Unable to set Firebase Analytics user property.",
            cause = cause
        )
    }

    fun resetFailure(cause: Throwable): SdkError {
        return SdkError(
            code = FirebaseAnalyticsErrorCodes.RESET_FAILED,
            message = "Unable to reset Firebase Analytics data.",
            cause = cause
        )
    }
}
