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
 * Stable error codes returned by Airship notification bridge APIs.
 */
object AirshipNotificationErrorCodes {
    /**
     * Fallback code for unexpected Airship notification bridge failures.
     */
    const val UNKNOWN = "airship_notifications_unknown"
    /**
     * Airship has not produced a channel ID for this app install.
     */
    const val CHANNEL_ID_UNAVAILABLE = "airship_notifications_channel_id_unavailable"
    /**
     * Airship channel lookup failed.
     */
    const val CHANNEL_LOOKUP_FAILED = "airship_notifications_channel_lookup_failed"
    /**
     * Airship channel deletion is not supported by this bridge.
     */
    const val CHANNEL_DELETE_UNSUPPORTED = "airship_notifications_channel_delete_unsupported"
    /**
     * Reading Airship user notification status failed.
     */
    const val USER_NOTIFICATIONS_STATUS_FAILED = "airship_notifications_user_notifications_status_failed"
    /**
     * Updating Airship user notification status failed.
     */
    const val USER_NOTIFICATIONS_ENABLE_FAILED = "airship_notifications_user_notifications_enable_failed"
    /**
     * Reading Airship audience state failed.
     */
    const val AUDIENCE_LOOKUP_FAILED = "airship_notifications_audience_lookup_failed"
    /**
     * Updating Airship audience state failed.
     */
    const val AUDIENCE_UPDATE_FAILED = "airship_notifications_audience_update_failed"
    /**
     * Airship audience input was blank or otherwise invalid.
     */
    const val INVALID_AUDIENCE_VALUE = "airship_notifications_invalid_audience_value"

    /**
     * Airship push operation failed.
     */
    const val PUSH_OPERATION_FAILED = "airship_notifications_push_operation_failed"

    /**
     * Airship contact operation failed.
     */
    const val CONTACT_OPERATION_FAILED = "airship_notifications_contact_operation_failed"

    /**
     * Airship contact input was blank or otherwise invalid.
     */
    const val INVALID_CONTACT_VALUE = "airship_notifications_invalid_contact_value"

    /**
     * Airship privacy operation failed.
     */
    const val PRIVACY_OPERATION_FAILED = "airship_notifications_privacy_operation_failed"
}
