package com.norbertotaveras.lantern.backgroundwork.internal

import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkErrorCodes
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkRequest
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkType
import com.norbertotaveras.lantern.core.SdkError

internal object BackgroundWorkRequestValidator {
    fun validate(request: BackgroundWorkRequest): SdkError? {
        if (request.name.value.isBlank()) {
            return SdkError(
                code = BackgroundWorkErrorCodes.INVALID_WORK_NAME,
                message = "Background work name cannot be blank."
            )
        }

        if (request.initialDelayMillis < 0L) {
            return SdkError(
                code = BackgroundWorkErrorCodes.INVALID_INITIAL_DELAY,
                message = "Background work initial delay cannot be negative."
            )
        }

        val type = request.type
        if (type is BackgroundWorkType.Periodic && type.repeatIntervalMillis <= 0L) {
            return SdkError(
                code = BackgroundWorkErrorCodes.INVALID_INTERVAL,
                message = "Periodic background work interval must be greater than zero."
            )
        }

        return null
    }
}
