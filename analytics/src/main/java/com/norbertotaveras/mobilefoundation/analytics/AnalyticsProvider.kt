package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface AnalyticsProvider {
    suspend fun track(event: AnalyticsEvent): SdkResult<Unit>

    suspend fun setUserId(userId: AnalyticsUserId?): SdkResult<Unit>

    suspend fun setUserProperty(property: AnalyticsUserProperty): SdkResult<Unit>

    suspend fun reset(): SdkResult<Unit>
}
