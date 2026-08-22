package com.norbertotaveras.lantern.appversioning

import com.norbertotaveras.lantern.core.SdkResult

/**
 * Provides the current app version.
 */
interface AppVersionProvider {
    /**
     * Returns the current version for the running app or configured source.
     */
    suspend fun getCurrentVersion(): SdkResult<AppVersion>
}
