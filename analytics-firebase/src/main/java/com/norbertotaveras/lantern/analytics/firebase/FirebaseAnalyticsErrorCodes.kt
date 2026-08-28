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

package com.norbertotaveras.lantern.analytics.firebase

/**
 * Stable error codes returned by Firebase Analytics integration.
 */
object FirebaseAnalyticsErrorCodes {
    const val TRACK_FAILED = "firebase_analytics_track_failed"
    const val USER_ID_FAILED = "firebase_analytics_user_id_failed"
    const val USER_PROPERTY_FAILED = "firebase_analytics_user_property_failed"
    const val RESET_FAILED = "firebase_analytics_reset_failed"
}
