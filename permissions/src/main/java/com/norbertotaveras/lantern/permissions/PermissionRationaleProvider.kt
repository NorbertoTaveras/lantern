package com.norbertotaveras.lantern.permissions

/**
 * App-provided bridge for Android's rationale signal.
 *
 * Implement this with the hosting Activity or Fragment's rationale API so SDK permission states can
 * distinguish normal denial from likely permanent denial after a request.
 */
fun interface PermissionRationaleProvider {
    /**
     * Returns whether Android recommends showing rationale for [manifestPermission].
     */
    fun shouldShowRationale(manifestPermission: String): Boolean
}
