package com.norbertotaveras.mobilefoundation.remoteconfig

sealed interface RemoteConfigValue {
    data class BooleanValue(val value: Boolean) : RemoteConfigValue
    data class DoubleValue(val value: Double) : RemoteConfigValue
    data class LongValue(val value: Long) : RemoteConfigValue
    data class StringValue(val value: String) : RemoteConfigValue
}
