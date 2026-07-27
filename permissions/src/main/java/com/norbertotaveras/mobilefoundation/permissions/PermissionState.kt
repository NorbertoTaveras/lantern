package com.norbertotaveras.mobilefoundation.permissions

data class PermissionState(
    val permission: SdkPermission,
    val status: PermissionStatus,
    val shouldShowRationale: Boolean = false
) {
    val isGranted: Boolean = status == PermissionStatus.Granted
}
