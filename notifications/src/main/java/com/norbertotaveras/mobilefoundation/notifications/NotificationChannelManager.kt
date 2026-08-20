package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Creates and removes notification channels for a backing notification provider.
 */
interface NotificationChannelManager {
    /**
     * Creates or updates a notification channel from [config].
     */
    suspend fun createChannel(config: NotificationChannelConfig): SdkResult<Unit>

    /**
     * Deletes the notification channel identified by [id].
     */
    suspend fun deleteChannel(id: NotificationChannelId): SdkResult<Unit>
}
