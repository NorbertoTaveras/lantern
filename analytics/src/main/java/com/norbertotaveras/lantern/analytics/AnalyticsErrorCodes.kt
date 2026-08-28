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

package com.norbertotaveras.lantern.analytics

/**
 * Stable provider-neutral error codes returned by analytics APIs.
 */
object AnalyticsErrorCodes {
    const val INVALID_EVENT_NAME = "analytics_invalid_event_name"
    const val INVALID_PROPERTY_NAME = "analytics_invalid_property_name"
    const val INVALID_USER_ID = "analytics_invalid_user_id"
    const val TRACK_FAILED = "analytics_track_failed"
    const val USER_ID_FAILED = "analytics_user_id_failed"
    const val USER_PROPERTY_FAILED = "analytics_user_property_failed"
    const val RESET_FAILED = "analytics_reset_failed"
}
