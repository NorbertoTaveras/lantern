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

/**
 * App-owned Airship settings that Lantern can convert into Airship SDK configuration.
 *
 * Store credentials in the consuming app's secure build configuration or runtime configuration
 * source. Lantern does not own or ship app keys, app secrets, notification resources, or channel
 * identifiers.
 *
 * @property appKey Airship app key for the consuming application.
 * @property appSecret Airship app secret for the consuming application.
 * @property site Airship cloud site for data locality.
 * @property notificationIconResId Drawable resource ID for the small notification icon, or `null`
 * to use Airship defaults.
 * @property notificationLargeIconResId Drawable resource ID for the large notification icon, or
 * `null` to use Airship defaults.
 * @property notificationAccentColor Notification accent color integer, or `null` to use Airship
 * defaults.
 * @property notificationChannel Default Android notification channel ID, or `null` to use Airship
 * defaults.
 * @property promptForPermissionOnUserNotificationsEnabled Whether Airship should prompt for
 * notification runtime permission when user notifications are enabled on Android 13+.
 * @property userNotificationsEnabled Initial user-visible notification setting to apply after
 * Airship is ready, or `null` to leave the current Airship setting unchanged.
 * @property analyticsEnabled Whether Airship analytics should be enabled.
 */
class AirshipNotificationConfig(
    val appKey: String,
    val appSecret: String,
    val site: AirshipNotificationSite = AirshipNotificationSite.US,
    val notificationIconResId: Int? = null,
    val notificationLargeIconResId: Int? = null,
    val notificationAccentColor: Int? = null,
    val notificationChannel: String? = null,
    val promptForPermissionOnUserNotificationsEnabled: Boolean = true,
    val userNotificationsEnabled: Boolean? = null,
    val analyticsEnabled: Boolean = true
) {
    override fun toString(): String {
        return "AirshipNotificationConfig(" +
            "appKey=$appKey, " +
            "appSecret=<redacted>, " +
            "site=$site, " +
            "notificationIconResId=$notificationIconResId, " +
            "notificationLargeIconResId=$notificationLargeIconResId, " +
            "notificationAccentColor=$notificationAccentColor, " +
            "notificationChannel=$notificationChannel, " +
            "promptForPermissionOnUserNotificationsEnabled=$promptForPermissionOnUserNotificationsEnabled, " +
            "userNotificationsEnabled=$userNotificationsEnabled, " +
            "analyticsEnabled=$analyticsEnabled" +
            ")"
    }
}
