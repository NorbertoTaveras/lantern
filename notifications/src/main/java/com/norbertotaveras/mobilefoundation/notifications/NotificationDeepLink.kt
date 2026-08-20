package com.norbertotaveras.mobilefoundation.notifications

/**
 * Deep-link data carried by a notification payload.
 */
data class NotificationDeepLink(
    /**
     * URI to parse or route when the notification is opened.
     */
    val uri: String,
    /**
     * Optional app route hint from the payload.
     */
    val route: String? = null,
    /**
     * Optional deep-link parameters extracted from the payload.
     */
    val parameters: Map<String, String> = emptyMap()
)
