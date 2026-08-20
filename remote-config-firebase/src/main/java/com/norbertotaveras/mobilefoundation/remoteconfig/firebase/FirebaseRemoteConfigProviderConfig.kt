package com.norbertotaveras.mobilefoundation.remoteconfig.firebase

import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigKey
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigSettings

/**
 * Firebase Remote Config provider configuration.
 */
data class FirebaseRemoteConfigProviderConfig(
    val settings: RemoteConfigSettings = RemoteConfigSettings(),
    val valueTypes: Map<RemoteConfigKey, FirebaseRemoteConfigValueType> = emptyMap()
)
