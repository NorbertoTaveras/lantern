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

import android.content.Context
import com.urbanairship.Airship
import com.urbanairship.AirshipConfigOptions
import com.urbanairship.Autopilot

/**
 * Optional Airship Autopilot base class that wires Lantern Airship config into Airship takeoff.
 *
 * Register a subclass in the consuming app manifest using Airship's
 * `com.urbanairship.autopilot` metadata key. Override [createLanternAirshipConfig] to return
 * app-owned credentials and notification resources.
 */
abstract class LanternAirshipAutopilot : Autopilot() {
    final override fun createAirshipConfigOptions(context: Context): AirshipConfigOptions {
        return AirshipConfigOptionsFactory.create(createLanternAirshipConfig(context))
    }

    final override fun onAirshipReady(context: Context) {
        createLanternAirshipConfig(context).userNotificationsEnabled?.let { enabled ->
            Airship.push.userNotificationsEnabled = enabled
        }
        onLanternAirshipReady(context)
    }

    /**
     * Returns app-owned Airship settings used to build [AirshipConfigOptions].
     */
    protected abstract fun createLanternAirshipConfig(context: Context): AirshipNotificationConfig

    /**
     * Hook for app-specific Airship setup after Airship is ready.
     */
    protected open fun onLanternAirshipReady(context: Context) = Unit
}
