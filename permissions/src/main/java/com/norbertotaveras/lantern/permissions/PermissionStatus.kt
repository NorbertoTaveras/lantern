package com.norbertotaveras.lantern.permissions

/**
 * Normalized permission status returned by the permissions module.
 */
enum class PermissionStatus {
    /**
     * All required Android manifest permissions are granted or the permission is granted by platform version.
     */
    Granted,

    /**
     * At least one required Android manifest permission is denied.
     */
    Denied,

    /**
     * A permission request was explicitly denied and Android no longer recommends showing rationale.
     */
    PermanentlyDenied,

    /**
     * Permission state has not been resolved yet.
     */
    NotDetermined,

    /**
     * The SDK permission cannot be resolved on this platform or configuration.
     */
    Unsupported
}
