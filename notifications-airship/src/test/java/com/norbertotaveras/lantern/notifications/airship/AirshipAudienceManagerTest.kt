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
import org.junit.Assert.assertTrue
import org.junit.Test

class AirshipAudienceManagerTest {

    @Test
    fun getTagsReturnsCachedGatewayTags() = runBlocking {
        val manager = AirshipAudienceManager(
            FakeAirshipAudienceGateway(initialTags = setOf("premium", "beta"))
        )

        val result = manager.getTags()

        assertEquals(setOf("premium", "beta"), (result as SdkResult.Success).data)
    }

    @Test
    fun addAndRemoveTagsNormalizesBlankValues() = runBlocking {
        val gateway = FakeAirshipAudienceGateway()
        val manager = AirshipAudienceManager(gateway)

        manager.addTags(setOf(" premium ", "", "beta"))
        manager.removeTags(setOf("beta"))

        assertEquals(setOf("premium"), gateway.tags)
    }

    @Test
    fun setAndRemoveAttributeNormalizesName() = runBlocking {
        val gateway = FakeAirshipAudienceGateway()
        val manager = AirshipAudienceManager(gateway)
        val value = AirshipAudienceAttributeValue.StringValue("pro")

        manager.setAttribute(" plan ", value)
        manager.removeAttribute(" plan ")

        assertTrue(gateway.attributes.isEmpty())
    }

    @Test
    fun subscribeAndUnsubscribeListsNormalizesBlankValues() = runBlocking {
        val gateway = FakeAirshipAudienceGateway()
        val manager = AirshipAudienceManager(gateway)

        manager.subscribeToLists(setOf(" news ", "", "promos"))
        manager.unsubscribeFromLists(setOf("promos"))

        assertEquals(setOf("news"), gateway.subscribedLists)
    }

    @Test
    fun blankAudienceValuesReturnFailure() = runBlocking {
        val manager = AirshipAudienceManager(FakeAirshipAudienceGateway())

        val result = manager.addTags(setOf("", " "))

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.INVALID_AUDIENCE_VALUE, error.code)
        assertEquals("add_tags", error.metadata["operation"])
    }

    @Test
    fun updateFailurePreservesCauseAndOperation() = runBlocking {
        val failure = IllegalStateException("Airship unavailable")
        val manager = AirshipAudienceManager(
            FakeAirshipAudienceGateway(updateFailure = failure)
        )

        val result = manager.addTags(setOf("premium"))

        val error = (result as SdkResult.Failure).error
        assertEquals(AirshipNotificationErrorCodes.AUDIENCE_UPDATE_FAILED, error.code)
        assertEquals("add_tags", error.metadata["operation"])
        assertSame(failure, error.cause)
    }
}
