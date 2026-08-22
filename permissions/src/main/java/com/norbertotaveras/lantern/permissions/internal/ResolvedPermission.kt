package com.norbertotaveras.lantern.permissions.internal

import com.norbertotaveras.lantern.permissions.PermissionStatus
import com.norbertotaveras.lantern.permissions.SdkPermission

internal data class ResolvedPermission(
    val permission: SdkPermission,
    val manifestPermissions: List<String>,
    val fixedStatus: PermissionStatus? = null
)
