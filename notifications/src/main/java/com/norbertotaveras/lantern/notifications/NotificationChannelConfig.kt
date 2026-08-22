package com.norbertotaveras.lantern.notifications

/**
 * Provider-neutral notification channel definition.
 */
data class NotificationChannelConfig(
    /**
     * Stable channel identifier.
     */
    val id: NotificationChannelId,
    /**
     * User-visible channel name.
     */
    val name: String,
    /**
     * Optional user-visible channel description.
     */
    val description: String? = null,
    /**
     * Importance used when creating the platform channel.
     */
    val importance: NotificationChannelImportance = NotificationChannelImportance.Default,
    /**
     * Whether notifications in this channel may show an app icon badge.
     */
    val showBadge: Boolean = true,
    /**
     * Optional lightweight metadata for callers and provider implementations.
     */
    val metadata: Map<String, String> = emptyMap()
)
