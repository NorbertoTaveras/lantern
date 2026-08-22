package com.norbertotaveras.lantern.permissions

/**
 * App-provided bridge that launches Android permission UI.
 *
 * The SDK stays UI-independent, so apps should implement this with Activity Result APIs or another
 * lifecycle-aware permission launcher. Return a map from each requested Android manifest permission to
 * whether it was granted.
 */
fun interface PermissionRequestLauncher {
    /**
     * Requests [manifestPermissions] and returns grant results.
     *
     * Throwing a non-cancellation exception is converted into a [PermissionResult.error] by
     * [PermissionManager] implementations. Coroutine cancellation should be allowed to propagate.
     */
    suspend fun request(manifestPermissions: List<String>): Map<String, Boolean>
}
