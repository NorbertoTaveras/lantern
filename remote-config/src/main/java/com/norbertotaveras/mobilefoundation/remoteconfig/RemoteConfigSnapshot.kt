package com.norbertotaveras.mobilefoundation.remoteconfig

data class RemoteConfigSnapshot(
    val values: Map<RemoteConfigKey, RemoteConfigValue>,
    val fetchStatus: RemoteConfigFetchStatus? = null,
    val activatedAtMillis: Long? = null
) {
    fun valueFor(key: RemoteConfigKey): RemoteConfigValue? {
        return values[key]
    }

    companion object {
        val Empty = RemoteConfigSnapshot(values = emptyMap())
    }
}
