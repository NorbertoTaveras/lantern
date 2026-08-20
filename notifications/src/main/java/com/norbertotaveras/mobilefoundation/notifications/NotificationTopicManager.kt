package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Subscribes and unsubscribes this app instance from provider notification topics.
 */
interface NotificationTopicManager {
    /**
     * Subscribes this app instance to [topic].
     */
    suspend fun subscribe(topic: NotificationTopic): SdkResult<Unit>

    /**
     * Unsubscribes this app instance from [topic].
     */
    suspend fun unsubscribe(topic: NotificationTopic): SdkResult<Unit>
}
