package com.norbertotaveras.mobilefoundation.notifications

data class NotificationPermissionState(
    val status: NotificationPermissionStatus,
    val canRequest: Boolean = status.canRequestByDefault
)
