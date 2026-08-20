package com.norbertotaveras.mobilefoundation.appversioning

/**
 * Result of applying an [AppUpdatePolicy] to the current app version.
 */
data class AppUpdateState(
    val currentVersion: AppVersion,
    val requirement: AppUpdateRequirement,
    val minimumSupportedVersion: AppVersion? = null,
    val latestVersion: AppVersion? = null
) {
    /**
     * True when the app should show either a soft or forced update flow.
     */
    val isUpdateRequired: Boolean
        get() = requirement != AppUpdateRequirement.None
}
