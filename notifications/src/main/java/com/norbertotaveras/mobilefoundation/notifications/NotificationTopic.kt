package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.notifications.internal.NotificationTopicValidator

@JvmInline
value class NotificationTopic private constructor(val value: String) {
    companion object {
        fun from(value: String): SdkResult<NotificationTopic> {
            return when (val result = NotificationTopicValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(NotificationTopic(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): NotificationTopic {
            return NotificationTopic(value)
        }
    }
}
