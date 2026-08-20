package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * [AppVersionProvider] that always returns a configured version.
 */
class StaticAppVersionProvider(
    private val currentVersion: AppVersion
) : AppVersionProvider {
    override suspend fun getCurrentVersion(): SdkResult<AppVersion> {
        return SdkResult.Success(currentVersion)
    }
}
