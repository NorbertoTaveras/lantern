package com.norbertotaveras.lantern.permissions

/**
 * Checks and requests Android runtime permissions using SDK-level permission types.
 *
 * Implementations return one state per [SdkPermission]. Duplicate permissions passed to multi-permission
 * methods are treated as one logical request.
 */
interface PermissionManager {
    /**
     * Returns the current state for [permission] without showing Android permission UI.
     */
    fun check(permission: SdkPermission): PermissionState

    /**
     * Returns current states for [permissions] without showing Android permission UI.
     */
    fun checkMultiple(permissions: List<SdkPermission>): Map<SdkPermission, PermissionState>

    /**
     * Requests [permission] and returns the resulting state.
     *
     * Implementations should preserve coroutine cancellation.
     */
    suspend fun request(permission: SdkPermission): PermissionResult

    /**
     * Requests [permissions] and returns the resulting states.
     *
     * Underlying manifest permissions may be deduplicated before launching Android permission UI.
     */
    suspend fun requestMultiple(permissions: List<SdkPermission>): PermissionResult
}
