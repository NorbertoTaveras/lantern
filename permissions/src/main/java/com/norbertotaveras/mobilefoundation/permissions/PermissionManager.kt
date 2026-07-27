package com.norbertotaveras.mobilefoundation.permissions

interface PermissionManager {
    fun check(permission: SdkPermission): PermissionState
    fun checkMultiple(permissions: List<SdkPermission>): Map<SdkPermission, PermissionState>
    suspend fun request(permission: SdkPermission): PermissionResult
    suspend fun requestMultiple(permissions: List<SdkPermission>): PermissionResult
}
