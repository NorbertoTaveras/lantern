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

import com.urbanairship.AirshipConfigOptions

/**
 * Creates Airship SDK configuration from Lantern Airship notification settings.
 */
object AirshipConfigOptionsFactory {
    /**
     * Builds [AirshipConfigOptions] from app-owned Lantern Airship notification config.
     */
    fun create(config: AirshipNotificationConfig): AirshipConfigOptions {
        val builder = AirshipConfigOptions.newBuilder()
            .setAppKey(config.appKey)
            .setAppSecret(config.appSecret)
            .setSite(toAirshipSite(config.site))
            .setAnalyticsEnabled(config.analyticsEnabled)
            .setIsPromptForPermissionOnUserNotificationsEnabled(
                config.promptForPermissionOnUserNotificationsEnabled
            )

        config.notificationIconResId?.let(builder::setNotificationIcon)
        config.notificationLargeIconResId?.let(builder::setNotificationLargeIcon)
        config.notificationAccentColor?.let(builder::setNotificationAccentColor)
        config.notificationChannel?.let(builder::setNotificationChannel)

        return builder.build()
    }

    private fun toAirshipSite(site: AirshipNotificationSite): AirshipConfigOptions.Site {
        return when (site) {
            AirshipNotificationSite.US -> AirshipConfigOptions.Site.SITE_US
            AirshipNotificationSite.EU -> AirshipConfigOptions.Site.SITE_EU
        }
    }
}
