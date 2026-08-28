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

package com.norbertotaveras.lantern.notifications

/**
 * Platform-neutral notification permission status.
 */
enum class NotificationPermissionStatus(
    /**
     * Default requestability for this status.
     */
    val canRequestByDefault: Boolean
) {
    /**
     * Permission is granted.
     */
    Granted(canRequestByDefault = false),
    /**
     * Permission is denied and may be requested again.
     */
    Denied(canRequestByDefault = true),
    /**
     * Permission has not been requested yet.
     */
    NotDetermined(canRequestByDefault = true),
    /**
     * Permission is denied and should be handled by directing the user to settings.
     */
    PermanentlyDenied(canRequestByDefault = false),
    /**
     * Runtime notification permission is not required on this platform version.
     */
    NotRequired(canRequestByDefault = false)
}
