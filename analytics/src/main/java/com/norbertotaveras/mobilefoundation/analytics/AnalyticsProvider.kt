package com.norbertotaveras.mobilefoundation.analytics

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Provider-neutral analytics contract.
 */
interface AnalyticsProvider {
    /**
     * Tracks an analytics [event].
     */
    suspend fun track(event: AnalyticsEvent): SdkResult<Unit>

    /**
     * Sets or clears the active analytics user ID.
     */
    suspend fun setUserId(userId: AnalyticsUserId?): SdkResult<Unit>

    /**
     * Sets a user-scoped analytics property.
     */
    suspend fun setUserProperty(property: AnalyticsUserProperty): SdkResult<Unit>

    /**
     * Clears provider analytics state for the current app instance.
     */
    suspend fun reset(): SdkResult<Unit>
}
