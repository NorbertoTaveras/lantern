package com.norbertotaveras.lantern.permissions

/**
 * Stable error codes returned by permission APIs.
 */
object PermissionErrorCodes {
    /**
     * Fallback code for unexpected permission failures.
     */
    const val UNKNOWN = "permission_unknown"
    /**
     * Permission request was attempted without a launcher.
     */
    const val REQUEST_UNAVAILABLE = "permission_request_unavailable"
    /**
     * A required permission was not declared in the app manifest.
     */
    const val PERMISSION_NOT_DECLARED = "permission_not_declared"
    /**
     * The requested SDK permission is not supported on the current platform version.
     */
    const val UNSUPPORTED_PERMISSION = "permission_unsupported"
}
