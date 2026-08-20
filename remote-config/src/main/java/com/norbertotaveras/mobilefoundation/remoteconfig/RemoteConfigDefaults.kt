package com.norbertotaveras.mobilefoundation.remoteconfig

/**
 * Default values applied before remote config values are fetched or activated.
 */
data class RemoteConfigDefaults(
    /**
     * Default values keyed by validated remote config keys.
     */
    val values: Map<RemoteConfigKey, RemoteConfigValue> = emptyMap()
) {
    /**
     * Returns merged defaults where values from [other] replace matching keys in this instance.
     */
    operator fun plus(other: RemoteConfigDefaults): RemoteConfigDefaults {
        return RemoteConfigDefaults(values + other.values)
    }

    companion object {
        /**
         * Empty defaults for providers that do not need fallback values.
         */
        val Empty = RemoteConfigDefaults()
    }
}
