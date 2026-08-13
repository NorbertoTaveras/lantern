package com.norbertotaveras.mobilefoundation.appversioning

data class AppUpdatePolicy(
    val minimumSupportedVersion: AppVersion? = null,
    val latestVersion: AppVersion? = null
)
