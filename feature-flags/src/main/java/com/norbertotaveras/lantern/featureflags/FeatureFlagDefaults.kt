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

import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue

/**
 * Default values for feature flags.
 */
data class FeatureFlagDefaults(
    /**
     * Default values keyed by feature flag key.
     */
    val values: Map<FeatureFlagKey, FeatureFlagValue> = emptyMap()
) {
    /**
     * Returns the configured default for [flag], or [FeatureFlag.defaultValue] when absent.
     */
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    /**
     * Returns merged defaults where values from [other] replace matching keys in this instance.
     */
    operator fun plus(other: FeatureFlagDefaults): FeatureFlagDefaults {
        return FeatureFlagDefaults(values + other.values)
    }

    /**
     * Converts feature flag defaults into remote config defaults for remote-backed providers.
     */
    fun toRemoteConfigDefaults(): RemoteConfigDefaults {
        return RemoteConfigDefaults(
            values = values.mapKeys { (key, _) ->
                RemoteConfigKey.unsafe(key.value)
            }.mapValues { (_, value) ->
                when (value) {
                    is FeatureFlagValue.BooleanValue -> RemoteConfigValue.BooleanValue(value.value)
                    is FeatureFlagValue.DoubleValue -> RemoteConfigValue.DoubleValue(value.value)
                    is FeatureFlagValue.LongValue -> RemoteConfigValue.LongValue(value.value)
                    is FeatureFlagValue.StringValue -> RemoteConfigValue.StringValue(value.value)
                }
            }
        )
    }

    companion object {
        /**
         * Empty defaults for providers that do not need fallback values.
         */
        val Empty = FeatureFlagDefaults()
    }
}
