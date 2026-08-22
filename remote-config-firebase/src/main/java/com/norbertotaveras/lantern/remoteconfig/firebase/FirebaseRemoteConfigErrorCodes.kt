package com.norbertotaveras.lantern.remoteconfig.firebase

/**
 * Stable error codes returned by Firebase Remote Config integration.
 */
object FirebaseRemoteConfigErrorCodes {
    const val UNKNOWN = "firebase_remote_config_unknown"
    const val FETCH_FAILED = "firebase_remote_config_fetch_failed"
    const val ACTIVATE_FAILED = "firebase_remote_config_activate_failed"
    const val DEFAULTS_FAILED = "firebase_remote_config_defaults_failed"
    const val SETTINGS_FAILED = "firebase_remote_config_settings_failed"
    const val VALUE_NOT_FOUND = "firebase_remote_config_value_not_found"
}
