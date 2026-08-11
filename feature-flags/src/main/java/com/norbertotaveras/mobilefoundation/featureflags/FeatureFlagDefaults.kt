package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigKey
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigValue

data class FeatureFlagDefaults(
    val values: Map<FeatureFlagKey, FeatureFlagValue> = emptyMap()
) {
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    operator fun plus(other: FeatureFlagDefaults): FeatureFlagDefaults {
        return FeatureFlagDefaults(values + other.values)
    }

    fun toRemoteConfigDefaults(): RemoteConfigDefaults {
        return RemoteConfigDefaults(
            values = values.mapKeys { (key, _) ->
                RemoteConfigKey.unsafe(key.value)
            }.mapValues { (_, value) ->
                when (value) {
                    is FeatureFlagValue.BooleanValue -> RemoteConfigValue.BooleanValue(value.value)
                    is FeatureFlagValue.DoubleValue -> RemoteConfigValue.DoubleValue(value.value)
                    is FeatureFlagValue.LongValue -> RemoteConfigValue.LongValue(value.value)
                    is FeatureFlagValue.StringValue -> RemoteConfigValue.StringValue(value.value)
                }
            }
        )
    }

    companion object {
        val Empty = FeatureFlagDefaults()
    }
}
