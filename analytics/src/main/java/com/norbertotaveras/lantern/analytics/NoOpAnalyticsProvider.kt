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

package com.norbertotaveras.lantern.analytics

import com.norbertotaveras.lantern.core.SdkResult

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
