package com.norbertotaveras.lantern.remoteconfig.firebase

import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigSettings

/**
 * Firebase Remote Config provider configuration.
 */
data class FirebaseRemoteConfigProviderConfig(
    val settings: RemoteConfigSettings = RemoteConfigSettings(),
    val valueTypes: Map<RemoteConfigKey, FirebaseRemoteConfigValueType> = emptyMap()
)
