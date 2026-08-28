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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * In-memory [FeatureFlagProvider] useful for tests, demos, and local-only flag sets.
 */
class StaticFeatureFlagProvider(
    initialValues: Map<FeatureFlagKey, FeatureFlagValue> = emptyMap()
) : FeatureFlagProvider {
    private val snapshotState = MutableStateFlow(FeatureFlagSnapshot(initialValues))

    override val updates: Flow<FeatureFlagSnapshot> = snapshotState.asStateFlow()

    /**
     * Replaces the active in-memory values and emits a new snapshot.
     */
    fun update(values: Map<FeatureFlagKey, FeatureFlagValue>) {
        snapshotState.value = FeatureFlagSnapshot(values)
    }

    override suspend fun evaluate(flag: FeatureFlag): SdkResult<FeatureFlagEvaluation> {
        val value = snapshotState.value.values[flag.key]
        return SdkResult.Success(
            FeatureFlagEvaluation(
                flag = flag,
                value = value ?: flag.defaultValue,
                source = if (value == null) FeatureFlagValueSource.Default else FeatureFlagValueSource.Provider
            )
        )
    }

    override suspend fun getSnapshot(): SdkResult<FeatureFlagSnapshot> {
        return SdkResult.Success(snapshotState.value)
    }
}
