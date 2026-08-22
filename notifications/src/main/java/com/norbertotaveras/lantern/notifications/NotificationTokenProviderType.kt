package com.norbertotaveras.lantern.notifications

/**
 * Provider that issued a [NotificationToken].
 */
enum class NotificationTokenProviderType {
    /**
     * Firebase Cloud Messaging token.
     */
    FirebaseCloudMessaging,
    /**
     * Caller-defined notification provider.
     */
    Custom
}
