package com.norbertotaveras.mobilefoundation.permissions

/**
 * Current SDK-level state for a permission.
 *
 * [shouldShowRationale] mirrors Android rationale guidance for the resolved manifest permissions.
 */
data class PermissionState(
    val permission: SdkPermission,
    val status: PermissionStatus,
    val shouldShowRationale: Boolean = false
) {
    /**
     * True when [status] is [PermissionStatus.Granted].
     */
    val isGranted: Boolean = status == PermissionStatus.Granted
}
