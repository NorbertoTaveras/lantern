package com.norbertotaveras.mobilefoundation.remoteconfig

data class RemoteConfigDefaults(
    val values: Map<RemoteConfigKey, RemoteConfigValue> = emptyMap()
) {
    operator fun plus(other: RemoteConfigDefaults): RemoteConfigDefaults {
        return RemoteConfigDefaults(values + other.values)
    }

    companion object {
        val Empty = RemoteConfigDefaults()
    }
}
