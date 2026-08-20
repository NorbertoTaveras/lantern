package com.norbertotaveras.mobilefoundation.remoteconfig

/**
 * Point-in-time view of active remote config values.
 */
data class RemoteConfigSnapshot(
    /**
     * Active values keyed by [RemoteConfigKey].
     */
    val values: Map<RemoteConfigKey, RemoteConfigValue>,
    /**
     * Most recent fetch status when known.
     */
    val fetchStatus: RemoteConfigFetchStatus? = null,
    /**
     * Epoch time when values were last activated, when known.
     */
    val activatedAtMillis: Long? = null
) {
    /**
     * Returns the value for [key], or `null` when the snapshot does not contain it.
     */
    fun valueFor(key: RemoteConfigKey): RemoteConfigValue? {
        return values[key]
    }

    companion object {
        /**
         * Empty snapshot used before values are available.
         */
        val Empty = RemoteConfigSnapshot(values = emptyMap())
    }
}
