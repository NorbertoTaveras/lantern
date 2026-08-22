package com.norbertotaveras.lantern.appversioning

import com.norbertotaveras.lantern.core.SdkResult

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
