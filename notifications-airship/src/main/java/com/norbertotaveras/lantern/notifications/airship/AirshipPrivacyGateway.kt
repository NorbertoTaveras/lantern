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

package com.norbertotaveras.lantern.notifications.airship

/**
 * Minimal Airship privacy/data collection surface consumed by Lantern helpers.
 */
interface AirshipPrivacyGateway {
    /**
     * Returns enabled Airship data collection features.
     */
    suspend fun getEnabledFeatures(): Set<AirshipPrivacyFeature>

    /**
     * Replaces enabled Airship data collection features.
     */
    suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>)

    /**
     * Adds enabled Airship data collection features.
     */
    suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>)

    /**
     * Removes enabled Airship data collection features.
     */
    suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>)
}
