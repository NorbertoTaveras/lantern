package com.norbertotaveras.mobilefoundation.appversioning

/**
 * Version policy used to decide whether an app update is needed.
 */
data class AppUpdatePolicy(
    val minimumSupportedVersion: AppVersion? = null,
    val latestVersion: AppVersion? = null
)
