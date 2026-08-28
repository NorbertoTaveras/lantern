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

package com.norbertotaveras.lantern.appversioning

/**
 * Result of applying an [AppUpdatePolicy] to the current app version.
 */
data class AppUpdateState(
    val currentVersion: AppVersion,
    val requirement: AppUpdateRequirement,
    val minimumSupportedVersion: AppVersion? = null,
    val latestVersion: AppVersion? = null
) {
    /**
     * True when the app should show either a soft or forced update flow.
     */
    val isUpdateRequired: Boolean
        get() = requirement != AppUpdateRequirement.None
}
