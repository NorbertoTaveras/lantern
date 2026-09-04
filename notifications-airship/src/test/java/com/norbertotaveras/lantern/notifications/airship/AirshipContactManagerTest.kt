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
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class AirshipContactManagerTest {

    @Test
    fun identifyAndResetUpdateGateway() = runBlocking {
        val gateway = FakeAirshipContactGateway()
        val manager = AirshipContactManager(gateway)

        manager.identify(" user-123 ")
        assertEquals("user-123", gateway.namedUserId)

        manager.reset()
        assertNull(gateway.namedUserId)
    }

    @Test
    fun setAndRemoveAttributeNormalizeName() = runBlocking {
        val gateway = FakeAirshipContactGateway()
        val manager = AirshipContactManager(gateway)
        val value = AirshipAudienceAttributeValue.StringValue("gold")

        manager.setAttribute(" plan ", value)
        manager.removeAttribute(" plan ")

        assertTrue(gateway.attributes.isEmpty())
    }

    @Test
    fun subscribeAndUnsubscribeListsNormalizeValues() = runBlocking {
        val gateway = FakeAirshipContactGateway()
        val manager = AirshipContactManager(gateway)

        manager.subscribeToLists(setOf(" weekly ", "", "promo"), AirshipContactSubscriptionScope.Email)
        manager.unsubscribeFromLists(setOf("promo"), AirshipContactSubscriptionScope.Email)

        assertEquals(setOf("weekly"), gateway.subscribedLists[AirshipContactSubscriptionScope.Email])
    }

    @Test
    fun blankNamedUserIdReturnsFailure() = runBlocking {
        val manager = AirshipContactManager(FakeAirshipContactGateway())

        val result = manager.identify(" ")

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.INVALID_CONTACT_VALUE, error.code)
        assertEquals("identify", error.metadata["operation"])
    }

    @Test
    fun operationFailurePreservesCauseAndOperation() = runBlocking {
        val failure = IllegalStateException("Airship unavailable")
        val manager = AirshipContactManager(FakeAirshipContactGateway(failure = failure))

        val result = manager.getNamedUserId()

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.CONTACT_OPERATION_FAILED, error.code)
        assertEquals("get_named_user_id", error.metadata["operation"])
        assertSame(failure, error.cause)
    }
}
