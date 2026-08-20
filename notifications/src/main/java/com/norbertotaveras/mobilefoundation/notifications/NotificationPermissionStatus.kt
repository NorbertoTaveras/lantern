package com.norbertotaveras.mobilefoundation.notifications

/**
 * Platform-neutral notification permission status.
 */
enum class NotificationPermissionStatus(
    /**
     * Default requestability for this status.
     */
    val canRequestByDefault: Boolean
) {
    /**
     * Permission is granted.
     */
    Granted(canRequestByDefault = false),
    /**
     * Permission is denied and may be requested again.
     */
    Denied(canRequestByDefault = true),
    /**
     * Permission has not been requested yet.
     */
    NotDetermined(canRequestByDefault = true),
    /**
     * Permission is denied and should be handled by directing the user to settings.
     */
    PermanentlyDenied(canRequestByDefault = false),
    /**
     * Runtime notification permission is not required on this platform version.
     */
    NotRequired(canRequestByDefault = false)
}
