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
 * Result of evaluating a [FeatureFlag].
 */
data class FeatureFlagEvaluation(
    /**
     * Flag that was evaluated.
     */
    val flag: FeatureFlag,
    /**
     * Resolved value for [flag].
     */
    val value: FeatureFlagValue,
    /**
     * Source of the resolved value.
     */
    val source: FeatureFlagValueSource
) {
    /**
     * Convenience helper for boolean flags.
     *
     * Non-boolean values are treated as disabled.
     */
    fun isEnabled(): Boolean {
        return (value as? FeatureFlagValue.BooleanValue)?.value ?: false
    }
}
