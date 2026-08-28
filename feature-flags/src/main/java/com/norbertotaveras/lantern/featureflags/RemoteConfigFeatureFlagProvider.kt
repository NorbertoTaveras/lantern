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
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigProvider
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Feature flag provider backed by [RemoteConfigProvider].
 *
 * Remote config keys are matched to feature flag keys by their raw string value.
 */
class RemoteConfigFeatureFlagProvider(
    private val remoteConfigProvider: RemoteConfigProvider
) : FeatureFlagProvider {

    override val updates: Flow<FeatureFlagSnapshot> = remoteConfigProvider.updates.map { snapshot ->
        FeatureFlagSnapshot(
            values = snapshot.values.mapKeys { (key, _) ->
                FeatureFlagKey.unsafe(key.value)
            }.mapValues { (_, value) ->
                when (value) {
                    is RemoteConfigValue.BooleanValue -> FeatureFlagValue.BooleanValue(value.value)
                    is RemoteConfigValue.DoubleValue -> FeatureFlagValue.DoubleValue(value.value)
                    is RemoteConfigValue.LongValue -> FeatureFlagValue.LongValue(value.value)
                    is RemoteConfigValue.StringValue -> FeatureFlagValue.StringValue(value.value)
                }
            }
        )
    }

    override suspend fun evaluate(flag: FeatureFlag): SdkResult<FeatureFlagEvaluation> {
        return when (val result = remoteConfigProvider.getSnapshot()) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> {
                val remoteValue = result.data.values.entries.firstOrNull { (key, _) ->
                    key.value == flag.key.value
                }?.value
                val value = when (remoteValue) {
                    is RemoteConfigValue.BooleanValue -> FeatureFlagValue.BooleanValue(remoteValue.value)
                    is RemoteConfigValue.DoubleValue -> FeatureFlagValue.DoubleValue(remoteValue.value)
                    is RemoteConfigValue.LongValue -> FeatureFlagValue.LongValue(remoteValue.value)
                    is RemoteConfigValue.StringValue -> FeatureFlagValue.StringValue(remoteValue.value)
                    null -> flag.defaultValue
                }
                SdkResult.Success(
                    FeatureFlagEvaluation(
                        flag = flag,
                        value = value,
                        source = if (remoteValue == null) {
                            FeatureFlagValueSource.Default
                        } else {
                            FeatureFlagValueSource.Provider
                        }
                    )
                )
            }
        }
    }

    override suspend fun getSnapshot(): SdkResult<FeatureFlagSnapshot> {
        return when (val result = remoteConfigProvider.getSnapshot()) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> SdkResult.Success(
                FeatureFlagSnapshot(
                    values = result.data.values.mapKeys { (key, _) ->
                        FeatureFlagKey.unsafe(key.value)
                    }.mapValues { (_, value) ->
                        when (value) {
                            is RemoteConfigValue.BooleanValue -> FeatureFlagValue.BooleanValue(value.value)
                            is RemoteConfigValue.DoubleValue -> FeatureFlagValue.DoubleValue(value.value)
                            is RemoteConfigValue.LongValue -> FeatureFlagValue.LongValue(value.value)
                            is RemoteConfigValue.StringValue -> FeatureFlagValue.StringValue(value.value)
                        }
                    }
                )
            )
        }
    }
}
