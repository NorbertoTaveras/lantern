package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkResult

class StaticAppVersionProvider(
    private val currentVersion: AppVersion
) : AppVersionProvider {
    override suspend fun getCurrentVersion(): SdkResult<AppVersion> {
        return SdkResult.Success(currentVersion)
    }
}
