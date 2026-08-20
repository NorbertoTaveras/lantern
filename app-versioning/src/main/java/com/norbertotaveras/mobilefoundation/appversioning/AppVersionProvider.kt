package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Provides the current app version.
 */
interface AppVersionProvider {
    /**
     * Returns the current version for the running app or configured source.
     */
    suspend fun getCurrentVersion(): SdkResult<AppVersion>
}
