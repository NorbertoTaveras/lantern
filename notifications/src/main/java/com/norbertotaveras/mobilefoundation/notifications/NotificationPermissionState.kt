package com.norbertotaveras.mobilefoundation.notifications

/**
 * Current notification permission state.
 */
data class NotificationPermissionState(
    /**
     * Permission status.
     */
    val status: NotificationPermissionStatus,
    /**
     * Whether the app can request notification permission from this state.
     */
    val canRequest: Boolean = status.canRequestByDefault
)
