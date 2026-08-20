package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Checks and requests notification permission when the platform requires it.
 */
interface NotificationPermissionManager {
    /**
     * Returns the current notification permission state without showing UI.
     */
    fun check(): NotificationPermissionState
    /**
     * Requests notification permission when supported by the platform.
     */
    suspend fun request(): SdkResult<NotificationPermissionState>
}
