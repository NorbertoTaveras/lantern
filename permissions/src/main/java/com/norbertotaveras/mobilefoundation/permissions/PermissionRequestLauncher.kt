package com.norbertotaveras.mobilefoundation.permissions

fun interface PermissionRequestLauncher {
    suspend fun request(manifestPermissions: List<String>): Map<String, Boolean>
}
