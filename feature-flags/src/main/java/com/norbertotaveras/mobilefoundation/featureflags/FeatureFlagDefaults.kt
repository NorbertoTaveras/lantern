package com.norbertotaveras.mobilefoundation.featureflags

import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigDefaults
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigKey
import com.norbertotaveras.mobilefoundation.remoteconfig.RemoteConfigValue

/**
 * Default values for feature flags.
 */
data class FeatureFlagDefaults(
    /**
     * Default values keyed by feature flag key.
     */
    val values: Map<FeatureFlagKey, FeatureFlagValue> = emptyMap()
) {
    /**
     * Returns the configured default for [flag], or [FeatureFlag.defaultValue] when absent.
     */
    fun valueFor(flag: FeatureFlag): FeatureFlagValue {
        return values[flag.key] ?: flag.defaultValue
    }

    /**
     * Returns merged defaults where values from [other] replace matching keys in this instance.
     */
    operator fun plus(other: FeatureFlagDefaults): FeatureFlagDefaults {
        return FeatureFlagDefaults(values + other.values)
    }

    /**
     * Converts feature flag defaults into remote config defaults for remote-backed providers.
     */
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
        /**
         * Empty defaults for providers that do not need fallback values.
         */
        val Empty = FeatureFlagDefaults()
    }
}
