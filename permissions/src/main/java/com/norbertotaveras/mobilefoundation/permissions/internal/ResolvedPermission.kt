package com.norbertotaveras.mobilefoundation.permissions.internal

import com.norbertotaveras.mobilefoundation.permissions.PermissionStatus
import com.norbertotaveras.mobilefoundation.permissions.SdkPermission

internal data class ResolvedPermission(
    val permission: SdkPermission,
    val manifestPermissions: List<String>,
    val fixedStatus: PermissionStatus? = null
)
