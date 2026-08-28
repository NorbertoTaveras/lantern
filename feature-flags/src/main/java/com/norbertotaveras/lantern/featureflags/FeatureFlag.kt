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
 * Describes a feature flag that can be evaluated by a [FeatureFlagProvider].
 */
data class FeatureFlag(
    /**
     * Stable key used to look up the flag value.
     */
    val key: FeatureFlagKey,
    /**
     * Value used when the provider does not return an override.
     */
    val defaultValue: FeatureFlagValue = FeatureFlagValue.BooleanValue(false),
    /**
     * Optional human-readable description for tooling or documentation.
     */
    val description: String? = null,
    /**
     * Optional lightweight metadata for callers and tooling.
     */
    val metadata: Map<String, String> = emptyMap()
)
