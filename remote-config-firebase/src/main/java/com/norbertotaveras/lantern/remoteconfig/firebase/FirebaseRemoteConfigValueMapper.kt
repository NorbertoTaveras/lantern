package com.norbertotaveras.lantern.remoteconfig.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfigValue
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigKey
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue

internal class FirebaseRemoteConfigValueMapper(
    private val valueTypes: Map<RemoteConfigKey, FirebaseRemoteConfigValueType> = emptyMap()
) {

    fun map(
        key: RemoteConfigKey,
        value: FirebaseRemoteConfigValue
    ): RemoteConfigValue {
        return when (valueTypes[key]) {
            FirebaseRemoteConfigValueType.Boolean -> RemoteConfigValue.BooleanValue(value.asBoolean())
            FirebaseRemoteConfigValueType.Double -> RemoteConfigValue.DoubleValue(value.asDouble())
            FirebaseRemoteConfigValueType.Long -> RemoteConfigValue.LongValue(value.asLong())
            FirebaseRemoteConfigValueType.String,
            null -> RemoteConfigValue.StringValue(value.asString())
        }
    }
}
