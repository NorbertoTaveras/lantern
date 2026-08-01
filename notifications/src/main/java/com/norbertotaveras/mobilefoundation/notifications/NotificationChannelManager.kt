package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface NotificationChannelManager {
    suspend fun createChannel(config: NotificationChannelConfig): SdkResult<Unit>

    suspend fun deleteChannel(id: NotificationChannelId): SdkResult<Unit>
}
