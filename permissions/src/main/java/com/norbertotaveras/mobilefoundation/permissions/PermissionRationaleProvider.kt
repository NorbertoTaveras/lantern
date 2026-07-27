package com.norbertotaveras.mobilefoundation.permissions

fun interface PermissionRationaleProvider {
    fun shouldShowRationale(manifestPermission: String): Boolean
}
