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

/**
 * Point-in-time view of active feature flag values.
 */
data class FeatureFlagSnapshot(
    /**
     * Active values keyed by [FeatureFlagKey].
     */
    val values: Map<FeatureFlagKey, FeatureFlagValue>
) {
    /**
     * Returns the active value for [flag], or [FeatureFlag.defaultValue] when absent.
     */
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    companion object {
        /**
         * Empty snapshot used before values are available.
         */
        val Empty = FeatureFlagSnapshot(values = emptyMap())
    }
}
