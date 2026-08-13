package com.norbertotaveras.mobilefoundation.appversioning

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface AppVersionProvider {
    suspend fun getCurrentVersion(): SdkResult<AppVersion>
}
