package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.notifications.internal.NotificationChannelIdValidator

/**
 * Validated notification channel identifier.
 */
@JvmInline
value class NotificationChannelId private constructor(val value: String) {
    companion object {
        /**
         * Creates a [NotificationChannelId] after trimming and validating [value].
         */
        @JvmStatic
        fun from(value: String): SdkResult<NotificationChannelId> {
            return when (val result = NotificationChannelIdValidator.validate(value)) {
                is SdkResult.Success -> SdkResult.Success(NotificationChannelId(result.data))
                is SdkResult.Failure -> result
            }
        }

        /**
         * Creates a [NotificationChannelId] without validation for trusted constants.
         */
        fun unsafe(value: String): NotificationChannelId {
            return NotificationChannelId(value)
        }
    }
}
