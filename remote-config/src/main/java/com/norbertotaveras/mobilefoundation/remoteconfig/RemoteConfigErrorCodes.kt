package com.norbertotaveras.mobilefoundation.remoteconfig

/**
 * Stable error codes returned by remote config APIs.
 */
object RemoteConfigErrorCodes {
    /**
     * Fallback code for unexpected remote config failures.
     */
    const val UNKNOWN = "remote_config_unknown"
    /**
     * A remote config key failed validation.
     */
    const val INVALID_KEY = "remote_config_invalid_key"
    /**
     * Fetching values from the backing provider failed.
     */
    const val FETCH_FAILED = "remote_config_fetch_failed"
    /**
     * Activating fetched values failed.
     */
    const val ACTIVATE_FAILED = "remote_config_activate_failed"
    /**
     * Applying provider defaults failed.
     */
    const val DEFAULTS_FAILED = "remote_config_defaults_failed"
    /**
     * A requested value was not found.
     */
    const val VALUE_NOT_FOUND = "remote_config_value_not_found"
    /**
     * A value exists but cannot be read as the requested type.
     */
    const val TYPE_MISMATCH = "remote_config_type_mismatch"
}
