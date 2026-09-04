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

internal class FakeAirshipPrivacyGateway(
    initialFeatures: Set<AirshipPrivacyFeature> = emptySet(),
    private val failure: Throwable? = null
) : AirshipPrivacyGateway {
    val enabledFeatures = initialFeatures.toMutableSet()

    override suspend fun getEnabledFeatures(): Set<AirshipPrivacyFeature> {
        failure?.let { throw it }
        return enabledFeatures.toSet()
    }

    override suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>) {
        failure?.let { throw it }
        enabledFeatures.clear()
        enabledFeatures.addAll(features)
    }

    override suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>) {
        failure?.let { throw it }
        enabledFeatures.addAll(features)
    }

    override suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>) {
        failure?.let { throw it }
        enabledFeatures.removeAll(features)
    }
}
