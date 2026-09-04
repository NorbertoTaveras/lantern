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

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AirshipConfigOptionsFactoryTest {

    @Test
    fun createMapsLanternConfigToAirshipConfigOptions() {
        val config = AirshipNotificationConfig(
            appKey = "airship-app-key",
            appSecret = "airship-app-secret",
            site = AirshipNotificationSite.EU,
            notificationIconResId = 12,
            notificationLargeIconResId = 34,
            notificationAccentColor = 0xFF1A73E8.toInt(),
            notificationChannel = "updates",
            promptForPermissionOnUserNotificationsEnabled = false,
            userNotificationsEnabled = true,
            analyticsEnabled = false
        )

        val options = AirshipConfigOptionsFactory.create(config)

        assertEquals("airship-app-key", options.appKey)
        assertEquals("airship-app-secret", options.appSecret)
        assertEquals(12, options.notificationIcon)
        assertEquals(34, options.notificationLargeIcon)
        assertEquals(0xFF1A73E8.toInt(), options.notificationAccentColor)
        assertEquals("updates", options.notificationChannel)
        assertFalse(options.isPromptForPermissionOnUserNotificationsEnabled)
        assertTrue(config.userNotificationsEnabled == true)
        assertFalse(options.analyticsEnabled)
    }

    @Test
    fun toStringRedactsAppSecret() {
        val config = AirshipNotificationConfig(
            appKey = "airship-app-key",
            appSecret = "airship-app-secret"
        )

        assertNotEquals(-1, config.toString().indexOf("appSecret=<redacted>"))
        assertEquals(-1, config.toString().indexOf("airship-app-secret"))
    }
}
