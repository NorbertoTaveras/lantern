package com.norbertotaveras.mobilefoundation.analytics

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
