package com.norbertotaveras.mobilefoundation.notifications

enum class NotificationPermissionStatus(
    val canRequestByDefault: Boolean
) {
    Granted(canRequestByDefault = false),
    Denied(canRequestByDefault = true),
    NotDetermined(canRequestByDefault = true),
    PermanentlyDenied(canRequestByDefault = false),
    NotRequired(canRequestByDefault = false)
}
