package com.norbertotaveras.mobilefoundation.notifications

/**
 * Provider token used to address this app instance for notifications.
 */
data class NotificationToken(
    /**
     * Raw provider token value.
     */
    val value: String,
    /**
     * Provider that issued the token.
     */
    val provider: NotificationTokenProviderType,
    /**
     * Epoch time when the token was created, when known.
     */
    val createdAtMillis: Long? = null,
    /**
     * Optional lightweight provider metadata.
     */
    val metadata: Map<String, String> = emptyMap()
)
