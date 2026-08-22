package com.norbertotaveras.lantern.remoteconfig

/**
 * Typed remote config value exposed by provider-neutral APIs.
 */
sealed interface RemoteConfigValue {
    /**
     * Boolean remote config value.
     */
    data class BooleanValue(val value: Boolean) : RemoteConfigValue
    /**
     * Double remote config value.
     */
    data class DoubleValue(val value: Double) : RemoteConfigValue
    /**
     * Long remote config value.
     */
    data class LongValue(val value: Long) : RemoteConfigValue
    /**
     * String remote config value.
     */
    data class StringValue(val value: String) : RemoteConfigValue
}
