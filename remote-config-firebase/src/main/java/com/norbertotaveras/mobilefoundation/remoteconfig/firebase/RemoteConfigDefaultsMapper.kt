package com.norbertotaveras.mobilefoundation.remoteconfig.firebase

import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigValue

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
