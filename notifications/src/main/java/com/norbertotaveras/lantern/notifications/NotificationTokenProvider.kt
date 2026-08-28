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

package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult
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
