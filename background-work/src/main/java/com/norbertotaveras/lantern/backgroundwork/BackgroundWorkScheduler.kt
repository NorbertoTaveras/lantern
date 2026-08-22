package com.norbertotaveras.lantern.backgroundwork

import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.flow.Flow

/**
 * Schedules, cancels, and observes background work.
 */
interface BackgroundWorkScheduler {
    /**
     * Enqueues [request] and returns the provider work ID.
     */
    suspend fun enqueue(request: BackgroundWorkRequest): SdkResult<BackgroundWorkId>

    /**
     * Cancels work by unique [name].
     */
    suspend fun cancel(name: BackgroundWorkName): SdkResult<Unit>

    /**
     * Returns the current work info for [name], or `null` when no work is known.
     */
    suspend fun getWorkInfo(name: BackgroundWorkName): SdkResult<BackgroundWorkInfo?>

    /**
     * Observes work info changes for [name].
     */
    fun observeWorkInfo(name: BackgroundWorkName): Flow<BackgroundWorkInfo?>
}
