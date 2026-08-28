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

package com.norbertotaveras.lantern.analytics.firebase

import com.norbertotaveras.lantern.core.SdkError

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
