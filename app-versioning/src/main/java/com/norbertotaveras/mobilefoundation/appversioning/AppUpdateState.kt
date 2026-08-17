package com.norbertotaveras.mobilefoundation.appversioning

data class AppUpdateState(
    val currentVersion: AppVersion,
    val requirement: AppUpdateRequirement,
    val minimumSupportedVersion: AppVersion? = null,
    val latestVersion: AppVersion? = null
) {
    val isUpdateRequired: Boolean
        get() = requirement != AppUpdateRequirement.None
}
