package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

interface NotificationTokenProvider {
    val tokenUpdates: Flow<NotificationToken?>

    suspend fun getToken(): SdkResult<NotificationToken>

    suspend fun deleteToken(): SdkResult<Unit>
}
