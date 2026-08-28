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

package com.norbertotaveras.lantern.featureflags

import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.flow.Flow

/**
 * Provider-neutral feature flag evaluator.
 */
interface FeatureFlagProvider {
    /**
     * Emits snapshots when active flag values change.
     */
    val updates: Flow<FeatureFlagSnapshot>

    /**
     * Evaluates [flag] and reports whether the resolved value came from the provider or default.
     */
    suspend fun evaluate(flag: FeatureFlag): SdkResult<FeatureFlagEvaluation>

    /**
     * Convenience helper for boolean flags.
     *
     * Non-boolean evaluated values are treated as disabled.
     */
    suspend fun isEnabled(flag: FeatureFlag): SdkResult<Boolean> {
        return when (val result = evaluate(flag)) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> SdkResult.Success(result.data.isEnabled())
        }
    }

    /**
     * Returns a point-in-time snapshot of active flag values.
     */
    suspend fun getSnapshot(): SdkResult<FeatureFlagSnapshot>
}
