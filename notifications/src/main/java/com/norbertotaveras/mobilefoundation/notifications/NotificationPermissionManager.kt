package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface NotificationPermissionManager {
    fun check(): NotificationPermissionState
    suspend fun request(): SdkResult<NotificationPermissionState>
}
