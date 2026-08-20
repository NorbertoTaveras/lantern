package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Analytics provider that accepts calls and intentionally performs no tracking.
 */
class NoOpAnalyticsProvider : AnalyticsProvider {
    override suspend fun track(event: AnalyticsEvent): SdkResult<Unit> {
        return SdkResult.Success(Unit)
    }

    override suspend fun setUserId(userId: AnalyticsUserId?): SdkResult<Unit> {
        return SdkResult.Success(Unit)
    }

    override suspend fun setUserProperty(property: AnalyticsUserProperty): SdkResult<Unit> {
        return SdkResult.Success(Unit)
    }

    override suspend fun reset(): SdkResult<Unit> {
        return SdkResult.Success(Unit)
    }
}
