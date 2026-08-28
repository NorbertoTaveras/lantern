/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
