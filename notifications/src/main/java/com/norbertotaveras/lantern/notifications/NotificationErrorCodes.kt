package com.norbertotaveras.lantern.notifications

/**
 * Stable error codes returned by notification APIs.
 */
object NotificationErrorCodes {
    /**
     * Fallback code for unexpected notification failures.
     */
    const val UNKNOWN = "notifications_unknown"
    /**
     * Notification permission is denied.
     */
    const val PERMISSION_DENIED = "notifications_permission_denied"
    /**
     * A provider token could not be returned.
     */
    const val TOKEN_UNAVAILABLE = "notifications_token_unavailable"
    /**
     * A topic failed validation.
     */
    const val INVALID_TOPIC = "notifications_invalid_topic"
    /**
     * Topic subscribe or unsubscribe failed.
     */
    const val TOPIC_SUBSCRIPTION_FAILED = "notifications_topic_subscription_failed"
    /**
     * A channel ID failed validation.
     */
    const val INVALID_CHANNEL_ID = "notifications_invalid_channel_id"
    /**
     * A channel create, update, or delete operation failed.
     */
    const val CHANNEL_OPERATION_FAILED = "notifications_channel_operation_failed"
    /**
     * A notification payload could not be parsed.
     */
    const val INVALID_PAYLOAD = "notifications_invalid_payload"
}
