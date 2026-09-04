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

internal class FakeAirshipPushGateway(
    var channelId: String? = null,
    var userNotificationsEnabled: Boolean = false,
    private val channelIdFailure: Throwable? = null,
    private val statusFailure: Throwable? = null,
    private val setUserNotificationsFailure: Throwable? = null
) : AirshipPushGateway {

    override suspend fun getChannelId(): String? {
        channelIdFailure?.let { throw it }
        return channelId
    }

    override suspend fun areUserNotificationsEnabled(): Boolean {
        statusFailure?.let { throw it }
        return userNotificationsEnabled
    }

    override suspend fun setUserNotificationsEnabled(enabled: Boolean) {
        setUserNotificationsFailure?.let { throw it }
        userNotificationsEnabled = enabled
    }
}
