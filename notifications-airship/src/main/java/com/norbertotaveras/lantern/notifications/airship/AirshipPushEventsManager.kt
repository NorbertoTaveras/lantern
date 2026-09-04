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
import com.norbertotaveras.lantern.notifications.NotificationChannelConfig
import kotlinx.coroutines.flow.Flow

/**
 * Reads and updates Airship push behavior through Lantern result contracts.
 */
class AirshipPushEventsManager(
    private val gateway: AirshipPushEventGateway
) {
    /**
     * Observes Airship push event callbacks.
     */
    fun observePushEvents(): Flow<AirshipPushEvent> {
        return gateway.observePushEvents()
    }

    /**
     * Returns the current Airship push notification status.
     */
    suspend fun getPushNotificationStatus(): SdkResult<AirshipPushNotificationStatus> {
        return runOperation("get_push_notification_status") {
            gateway.getPushNotificationStatus()
        }
    }

    /**
     * Creates an Airship notification channel.
     */
    suspend fun createNotificationChannel(config: NotificationChannelConfig): SdkResult<Unit> {
        return runOperation("create_notification_channel") {
            gateway.createNotificationChannel(config)
        }
    }

    /**
     * Enables or disables Airship foreground notification display.
     */
    suspend fun setForegroundNotificationDisplayEnabled(enabled: Boolean): SdkResult<Unit> {
        return runOperation("set_foreground_notification_display") {
            gateway.setForegroundNotificationDisplayEnabled(enabled)
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
                    code = AirshipNotificationErrorCodes.PUSH_OPERATION_FAILED,
                    message = "Unable to complete Airship push operation.",
                    cause = throwable,
                    metadata = mapOf("operation" to operation)
                )
            )
        }
    }
}
