package com.norbertotaveras.mobilefoundation.analytics

/**
 * Typed analytics parameter or user property value.
 */
sealed interface AnalyticsValue {
    data class BooleanValue(val value: Boolean) : AnalyticsValue
    data class DoubleValue(val value: Double) : AnalyticsValue
    data class LongValue(val value: Long) : AnalyticsValue
    data class StringValue(val value: String) : AnalyticsValue
}
