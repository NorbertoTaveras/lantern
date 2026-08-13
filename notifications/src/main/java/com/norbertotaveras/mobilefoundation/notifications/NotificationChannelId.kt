package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.notifications.internal.NotificationChannelIdValidator

@JvmInline
value class NotificationChannelId private constructor(val value: String) {
    companion object {
        @JvmStatic
        fun from(value: String): SdkResult<NotificationChannelId> {
            return when (val result = NotificationChannelIdValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(NotificationChannelId(result.data))
                is SdkResult.Failure -> result
            }
        }

        fun unsafe(value: String): NotificationChannelId {
            return NotificationChannelId(value)
        }
    }
}
