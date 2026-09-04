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

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult

/**
 * Updates Airship privacy/data collection features through Lantern result contracts.
 */
class AirshipPrivacyManager(
    private val gateway: AirshipPrivacyGateway
) {
    /**
     * Returns enabled Airship features.
     */
    suspend fun getEnabledFeatures(): SdkResult<Set<AirshipPrivacyFeature>> {
        return runOperation("get_enabled_features") {
            gateway.getEnabledFeatures()
        }
    }

    /**
     * Replaces enabled Airship features.
     */
    suspend fun setEnabledFeatures(features: Set<AirshipPrivacyFeature>): SdkResult<Unit> {
        return runOperation("set_enabled_features") {
            gateway.setEnabledFeatures(features)
        }
    }

    /**
     * Enables Airship features without disabling currently enabled features.
     */
    suspend fun enableFeatures(features: Set<AirshipPrivacyFeature>): SdkResult<Unit> {
        return runOperation("enable_features") {
            gateway.enableFeatures(features)
        }
    }

    /**
     * Disables Airship features while leaving other features unchanged.
     */
    suspend fun disableFeatures(features: Set<AirshipPrivacyFeature>): SdkResult<Unit> {
        return runOperation("disable_features") {
            gateway.disableFeatures(features)
        }
    }

    private suspend fun <T> runOperation(
        operation: String,
        block: suspend () -> T
    ): SdkResult<T> {
        return try {
            SdkResult.Success(block())
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = AirshipNotificationErrorCodes.PRIVACY_OPERATION_FAILED,
                    message = "Unable to complete Airship privacy operation.",
                    cause = throwable,
                    metadata = mapOf("operation" to operation)
                )
            )
        }
    }
}
