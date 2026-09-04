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

import com.urbanairship.Airship
import com.urbanairship.PrivacyManager

/**
 * [AirshipPrivacyGateway] backed by the Airship Android SDK singleton.
 */
class AirshipSdkPrivacyGateway : AirshipPrivacyGateway {
    override suspend fun getEnabledFeatures(): Set<AirshipPrivacyFeature> {
        return AirshipPrivacyFeature.entries
            .filter { feature -> Airship.privacyManager.isEnabled(feature.toAirshipFeature()) }
            .toSet()
    }

    override suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>) {
        Airship.privacyManager.setEnabledFeatures(*features.toAirshipFeatures())
    }

    override suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>) {
        Airship.privacyManager.enable(*features.toAirshipFeatures())
    }

    override suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>) {
        Airship.privacyManager.disable(*features.toAirshipFeatures())
    }

    private fun Set<AirshipPrivacyFeature>.toAirshipFeatures(): Array<PrivacyManager.Feature> {
        return map { feature -> feature.toAirshipFeature() }.toTypedArray()
    }

    private fun AirshipPrivacyFeature.toAirshipFeature(): PrivacyManager.Feature {
        return when (this) {
            AirshipPrivacyFeature.Push -> PrivacyManager.Feature.PUSH
            AirshipPrivacyFeature.Analytics -> PrivacyManager.Feature.ANALYTICS
            AirshipPrivacyFeature.TagsAndAttributes -> PrivacyManager.Feature.TAGS_AND_ATTRIBUTES
            AirshipPrivacyFeature.Contacts -> PrivacyManager.Feature.CONTACTS
            AirshipPrivacyFeature.FeatureFlags -> PrivacyManager.Feature.FEATURE_FLAGS
            AirshipPrivacyFeature.MessageCenter -> PrivacyManager.Feature.MESSAGE_CENTER
            AirshipPrivacyFeature.InAppAutomation -> PrivacyManager.Feature.IN_APP_AUTOMATION
        }
    }
}
