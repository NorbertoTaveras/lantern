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

import com.urbanairship.Airship

/**
 * [AirshipPushGateway] backed by the Airship Android SDK singleton.
 *
 * The consuming app must initialize Airship and configure its push provider before this gateway
 * is used. Lantern reads Airship state through this adapter but does not own Airship credentials,
 * manifest metadata, or Firebase Cloud Messaging setup.
 */
class AirshipSdkPushGateway : AirshipPushGateway {
    override suspend fun getChannelId(): String? {
        return Airship.channel.id
    }

    override suspend fun areUserNotificationsEnabled(): Boolean {
        return Airship.push.userNotificationsEnabled
    }

    override suspend fun setUserNotificationsEnabled(enabled: Boolean) {
        Airship.push.userNotificationsEnabled = enabled
    }
}
