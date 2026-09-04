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
 * Applies Airship channel audience updates through Lantern result contracts.
 */
class AirshipAudienceManager(
    private val gateway: AirshipAudienceGateway
) {
    /**
     * Returns the currently cached Airship channel tags.
     */
    suspend fun getTags(): SdkResult<Set<String>> {
        return try {
            SdkResult.Success(gateway.getTags())
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                operationFailure(
                    code = AirshipNotificationErrorCodes.AUDIENCE_LOOKUP_FAILED,
                    operation = "get_tags",
                    throwable = throwable
                )
            )
        }
    }

    /**
     * Adds tags to the Airship channel.
     */
    suspend fun addTags(tags: Set<String>): SdkResult<Unit> {
        return updateAudience("add_tags", tags) {
            gateway.addTags(normalizeValues(tags))
        }
    }

    /**
     * Removes tags from the Airship channel.
     */
    suspend fun removeTags(tags: Set<String>): SdkResult<Unit> {
        return updateAudience("remove_tags", tags) {
            gateway.removeTags(normalizeValues(tags))
        }
    }

    /**
     * Clears all tags from the Airship channel.
     */
    suspend fun clearTags(): SdkResult<Unit> {
        return updateAudience("clear_tags") {
            gateway.clearTags()
        }
    }

    /**
     * Sets an Airship channel attribute.
     */
    suspend fun setAttribute(
        name: String,
        value: AirshipAudienceAttributeValue
    ): SdkResult<Unit> {
        val normalizedName = name.trim()
        if (normalizedName.isEmpty()) {
            return invalidValue("set_attribute", "Attribute name must not be blank.")
        }

        return updateAudience("set_attribute") {
            gateway.setAttribute(normalizedName, value)
        }
    }

    /**
     * Removes an Airship channel attribute.
     */
    suspend fun removeAttribute(name: String): SdkResult<Unit> {
        val normalizedName = name.trim()
        if (normalizedName.isEmpty()) {
            return invalidValue("remove_attribute", "Attribute name must not be blank.")
        }

        return updateAudience("remove_attribute") {
            gateway.removeAttribute(normalizedName)
        }
    }

    /**
     * Subscribes the Airship channel to subscription lists.
     */
    suspend fun subscribeToLists(listIds: Set<String>): SdkResult<Unit> {
        return updateAudience("subscribe_to_lists", listIds) {
            gateway.subscribeToLists(normalizeValues(listIds))
        }
    }

    /**
     * Unsubscribes the Airship channel from subscription lists.
     */
    suspend fun unsubscribeFromLists(listIds: Set<String>): SdkResult<Unit> {
        return updateAudience("unsubscribe_from_lists", listIds) {
            gateway.unsubscribeFromLists(normalizeValues(listIds))
        }
    }

    private suspend fun updateAudience(
        operation: String,
        values: Set<String>? = null,
        block: suspend () -> Unit
    ): SdkResult<Unit> {
        if (values != null && normalizeValues(values).isEmpty()) {
            return invalidValue(operation, "At least one non-blank value is required.")
        }

        return try {
            block()
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                operationFailure(
                    code = AirshipNotificationErrorCodes.AUDIENCE_UPDATE_FAILED,
                    operation = operation,
                    throwable = throwable
                )
            )
        }
    }

    private fun normalizeValues(values: Set<String>): Set<String> {
        return values.map(String::trim)
            .filter(String::isNotEmpty)
            .toSet()
    }

    private fun invalidValue(operation: String, message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = AirshipNotificationErrorCodes.INVALID_AUDIENCE_VALUE,
                message = message,
                metadata = mapOf("operation" to operation)
            )
        )
    }

    private fun operationFailure(
        code: String,
        operation: String,
        throwable: Throwable
    ): SdkError {
        return SdkError(
            code = code,
            message = if (code == AirshipNotificationErrorCodes.AUDIENCE_LOOKUP_FAILED) {
                "Unable to load Airship audience state."
            } else {
                "Unable to update Airship audience state."
            },
            cause = throwable,
            metadata = mapOf("operation" to operation)
        )
    }
}
