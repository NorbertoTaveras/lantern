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
 * Reads and updates Airship contact identity through Lantern result contracts.
 */
class AirshipContactManager(
    private val gateway: AirshipContactGateway
) {
    /**
     * Returns the current Airship named user ID.
     */
    suspend fun getNamedUserId(): SdkResult<String?> {
        return runOperation("get_named_user_id") {
            gateway.getNamedUserId()
        }
    }

    /**
     * Identifies the Airship contact with [namedUserId].
     */
    suspend fun identify(namedUserId: String): SdkResult<Unit> {
        val normalizedId = namedUserId.trim()
        if (normalizedId.isEmpty()) {
            return invalidValue("identify", "Named user ID must not be blank.")
        }
        return runOperation("identify") {
            gateway.identify(normalizedId)
        }
    }

    /**
     * Resets the Airship contact back to anonymous state.
     */
    suspend fun reset(): SdkResult<Unit> {
        return runOperation("reset") {
            gateway.reset()
        }
    }

    /**
     * Sets an Airship contact attribute.
     */
    suspend fun setAttribute(
        name: String,
        value: AirshipAudienceAttributeValue
    ): SdkResult<Unit> {
        val normalizedName = name.trim()
        if (normalizedName.isEmpty()) {
            return invalidValue("set_attribute", "Attribute name must not be blank.")
        }
        return runOperation("set_attribute") {
            gateway.setAttribute(normalizedName, value)
        }
    }

    /**
     * Removes an Airship contact attribute.
     */
    suspend fun removeAttribute(name: String): SdkResult<Unit> {
        val normalizedName = name.trim()
        if (normalizedName.isEmpty()) {
            return invalidValue("remove_attribute", "Attribute name must not be blank.")
        }
        return runOperation("remove_attribute") {
            gateway.removeAttribute(normalizedName)
        }
    }

    /**
     * Subscribes the Airship contact to [listIds] for [scope].
     */
    suspend fun subscribeToLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ): SdkResult<Unit> {
        return runListOperation("subscribe_to_lists", listIds) { normalizedListIds ->
            gateway.subscribeToLists(normalizedListIds, scope)
        }
    }

    /**
     * Unsubscribes the Airship contact from [listIds] for [scope].
     */
    suspend fun unsubscribeFromLists(
        listIds: Set<String>,
        scope: AirshipContactSubscriptionScope
    ): SdkResult<Unit> {
        return runListOperation("unsubscribe_from_lists", listIds) { normalizedListIds ->
            gateway.unsubscribeFromLists(normalizedListIds, scope)
        }
    }

    private suspend fun runListOperation(
        operation: String,
        values: Set<String>,
        block: suspend (Set<String>) -> Unit
    ): SdkResult<Unit> {
        val normalizedValues = values.map(String::trim)
            .filter(String::isNotEmpty)
            .toSet()
        if (normalizedValues.isEmpty()) {
            return invalidValue(operation, "At least one non-blank subscription list ID is required.")
        }
        return runOperation(operation) {
            block(normalizedValues)
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
                    code = AirshipNotificationErrorCodes.CONTACT_OPERATION_FAILED,
                    message = "Unable to complete Airship contact operation.",
                    cause = throwable,
                    metadata = mapOf("operation" to operation)
                )
            )
        }
    }

    private fun invalidValue(operation: String, message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = AirshipNotificationErrorCodes.INVALID_CONTACT_VALUE,
                message = message,
                metadata = mapOf("operation" to operation)
            )
        )
    }
}
