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
 * Reads and updates Airship user-visible notification enablement through a gateway.
 */
class AirshipUserNotificationsManager(
    private val gateway: AirshipPushGateway
) {
    /**
     * Returns the current Airship notification status.
     */
    suspend fun getStatus(): SdkResult<AirshipNotificationStatus> {
        return try {
            SdkResult.Success(
                AirshipNotificationStatus(
                    channelId = gateway.getChannelId(),
                    userNotificationsEnabled = gateway.areUserNotificationsEnabled()
                )
            )
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = AirshipNotificationErrorCodes.USER_NOTIFICATIONS_STATUS_FAILED,
                    message = "Unable to load Airship notification status.",
                    cause = throwable
                )
            )
        }
    }

    /**
     * Enables Airship user-visible notifications.
     */
    suspend fun enableUserNotifications(): SdkResult<Unit> {
        return setUserNotificationsEnabled(true)
    }

    /**
     * Disables Airship user-visible notifications.
     */
    suspend fun disableUserNotifications(): SdkResult<Unit> {
        return setUserNotificationsEnabled(false)
    }

    private suspend fun setUserNotificationsEnabled(enabled: Boolean): SdkResult<Unit> {
        return try {
            gateway.setUserNotificationsEnabled(enabled)
            SdkResult.Success(Unit)
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = AirshipNotificationErrorCodes.USER_NOTIFICATIONS_ENABLE_FAILED,
                    message = "Unable to update Airship user notification setting.",
                    cause = throwable
                )
            )
        }
    }
}
