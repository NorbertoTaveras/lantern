package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import kotlinx.coroutines.flow.Flow

/**
 * Provider-neutral notification token contract.
 */
interface NotificationTokenProvider {
    /**
     * Emits token changes. `null` means no active token is available.
     */
    val tokenUpdates: Flow<NotificationToken?>

    /**
     * Returns the current provider token, requesting or refreshing it if the implementation requires.
     */
    suspend fun getToken(): SdkResult<NotificationToken>

    /**
     * Deletes the current provider token.
     */
    suspend fun deleteToken(): SdkResult<Unit>
}
