package com.norbertotaveras.lantern.notifications

/**
 * Normalized notification payload data.
 */
data class NotificationPayload(
    /**
     * Optional notification title.
     */
    val title: String? = null,
    /**
     * Optional notification body.
     */
    val body: String? = null,
    /**
     * Optional deep-link data included in the notification.
     */
    val deepLink: NotificationDeepLink? = null,
    /**
     * Original key-value payload.
     */
    val data: Map<String, String> = emptyMap()
)
