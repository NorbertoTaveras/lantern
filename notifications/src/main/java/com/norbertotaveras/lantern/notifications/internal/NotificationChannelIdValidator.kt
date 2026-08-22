package com.norbertotaveras.lantern.notifications.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.notifications.NotificationErrorCodes

internal object NotificationChannelIdValidator {
    private val channelIdPattern = Regex("^[A-Za-z][A-Za-z0-9_.-]{0,127}$")

    fun validate(value: String): SdkResult<String> {
        val normalized = value.trim()
        if (channelIdPattern.matches(normalized)) {
            return SdkResult.Success(normalized)
        }

        return SdkResult.Failure(
            SdkError(
                code = NotificationErrorCodes.INVALID_CHANNEL_ID,
                message = "Notification channel id must start with a letter and contain only letters, numbers, '_', '-', or '.'."
            )
        )
    }
}
