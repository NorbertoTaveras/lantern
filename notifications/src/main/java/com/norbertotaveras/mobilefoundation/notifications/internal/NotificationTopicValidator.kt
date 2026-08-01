package com.norbertotaveras.mobilefoundation.notifications.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.notifications.NotificationErrorCodes

internal object NotificationTopicValidator {
    private val topicPattern = Regex("^[A-Za-z0-9_.~%-]{1,900}$")

    fun validate(value: String): SdkResult<String> {
        val normalized = value.trim()
        if (topicPattern.matches(normalized)) {
            return SdkResult.Success(normalized)
        }

        return SdkResult.Failure(
            SdkError(
                code = NotificationErrorCodes.INVALID_TOPIC,
                message = "Notification topic must be 1-900 characters and contain only letters, numbers, '_', '-', '.', '~', or '%'."
            )
        )
    }
}
