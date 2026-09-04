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
import com.norbertotaveras.lantern.notifications.NotificationToken
import com.norbertotaveras.lantern.notifications.NotificationTokenProvider
import com.norbertotaveras.lantern.notifications.NotificationTokenProviderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * [NotificationTokenProvider] that exposes the Airship channel ID as the Lantern notification token.
 */
class AirshipNotificationTokenProvider(
    private val gateway: AirshipPushGateway
) : NotificationTokenProvider {

    private val tokenState = MutableStateFlow<NotificationToken?>(null)

    override val tokenUpdates: Flow<NotificationToken?> = tokenState.asStateFlow()

    override suspend fun getToken(): SdkResult<NotificationToken> {
        return try {
            val channelId = gateway.getChannelId()
            if (channelId.isNullOrBlank()) {
                tokenState.value = null
                SdkResult.Failure(channelUnavailable())
            } else {
                val token = NotificationToken(
                    value = channelId,
                    provider = NotificationTokenProviderType.Airship,
                    createdAtMillis = System.currentTimeMillis(),
                    metadata = mapOf("provider" to "airship")
                )
                tokenState.value = token
                SdkResult.Success(token)
            }
        } catch (throwable: Throwable) {
            SdkResult.Failure(
                SdkError(
                    code = AirshipNotificationErrorCodes.CHANNEL_LOOKUP_FAILED,
                    message = "Unable to load Airship channel ID.",
                    cause = throwable
                )
            )
        }
    }

    override suspend fun deleteToken(): SdkResult<Unit> {
        tokenState.value = null
        return SdkResult.Success(Unit)
    }

    private fun channelUnavailable(): SdkError {
        return SdkError(
            code = AirshipNotificationErrorCodes.CHANNEL_ID_UNAVAILABLE,
            message = "Airship channel ID is not available."
        )
    }
}
