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
 * Push lifecycle events surfaced from Airship.
 */
enum class AirshipPushEventType {
    /**
     * Push payload was received by Airship.
     */
    Received,
    /**
     * Airship posted a notification.
     */
    Posted,
    /**
     * User opened a posted notification.
     */
    Opened,
    /**
     * User dismissed a posted notification.
     */
    Dismissed,
    /**
     * User triggered a foreground notification action.
     */
    ForegroundAction,
    /**
     * User triggered a background notification action.
     */
    BackgroundAction,
    /**
     * Airship push token changed.
     */
    TokenUpdated,
    /**
     * Airship push notification status changed.
     */
    StatusChanged
}
