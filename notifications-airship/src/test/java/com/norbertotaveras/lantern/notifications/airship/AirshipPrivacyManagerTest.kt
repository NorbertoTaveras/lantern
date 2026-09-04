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

import com.norbertotaveras.lantern.core.SdkResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class AirshipPrivacyManagerTest {

    @Test
    fun setEnableAndDisableFeaturesUpdateGateway() = runBlocking {
        val gateway = FakeAirshipPrivacyGateway()
        val manager = AirshipPrivacyManager(gateway)

        manager.setEnabledFeatures(setOf(AirshipPrivacyFeature.Push))
        manager.enableFeatures(setOf(AirshipPrivacyFeature.Analytics))
        manager.disableFeatures(setOf(AirshipPrivacyFeature.Push))

        assertEquals(setOf(AirshipPrivacyFeature.Analytics), gateway.enabledFeatures)
    }

    @Test
    fun getEnabledFeaturesReturnsGatewayFeatures() = runBlocking {
        val manager = AirshipPrivacyManager(
            FakeAirshipPrivacyGateway(initialFeatures = setOf(AirshipPrivacyFeature.Push))
        )

        val result = manager.getEnabledFeatures()

        assertEquals(setOf(AirshipPrivacyFeature.Push), (result as SdkResult.Success).data)
    }

    @Test
    fun operationFailurePreservesCauseAndOperation() = runBlocking {
        val failure = IllegalStateException("Airship unavailable")
        val manager = AirshipPrivacyManager(FakeAirshipPrivacyGateway(failure = failure))

        val result = manager.getEnabledFeatures()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.PRIVACY_OPERATION_FAILED, error.code)
        assertEquals("get_enabled_features", error.metadata["operation"])
        assertSame(failure, error.cause)
    }
}
