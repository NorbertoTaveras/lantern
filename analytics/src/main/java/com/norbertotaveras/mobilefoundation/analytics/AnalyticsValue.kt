package com.norbertotaveras.mobilefoundation.analytics

/**
 * Typed analytics parameter or user property value.
 *
 * Use these variants instead of passing raw platform values so provider modules can map analytics
 * data consistently and reject unsupported value types before reaching vendor SDKs.
 */
sealed interface AnalyticsValue {
    /**
     * Boolean analytics value.
     */
    data class BooleanValue(val value: Boolean) : AnalyticsValue

    /**
     * Double analytics value.
     */
    data class DoubleValue(val value: Double) : AnalyticsValue

    /**
     * Long analytics value.
     */
    data class LongValue(val value: Long) : AnalyticsValue

    /**
     * String analytics value.
     */
    data class StringValue(val value: String) : AnalyticsValue
}
