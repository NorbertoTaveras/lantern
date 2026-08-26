package com.norbertotaveras.lantern.remoteconfig.firebase.internal

import com.norbertotaveras.lantern.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.lantern.remoteconfig.RemoteConfigValue

/**
 * Converts SDK remote config defaults into Firebase-supported default values.
 */
fun RemoteConfigDefaults.toFirebaseDefaults(): Map<String, Any> {
    return values.mapKeys { (key, _) -> key.value }
        .mapValues { (_, value) ->
            when (value) {
                is RemoteConfigValue.BooleanValue -> value.value
                is RemoteConfigValue.DoubleValue -> value.value
                is RemoteConfigValue.LongValue -> value.value
                is RemoteConfigValue.StringValue -> value.value
            }
        }
}
