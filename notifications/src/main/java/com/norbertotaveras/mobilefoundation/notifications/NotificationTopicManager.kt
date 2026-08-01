package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface NotificationTopicManager {
    suspend fun subscribe(topic: NotificationTopic): SdkResult<Unit>

    suspend fun unsubscribe(topic: NotificationTopic): SdkResult<Unit>
}
