package com.norbertotaveras.lantern.analytics

/**
 * Analytics user property with a validated name and typed value.
 */
data class AnalyticsUserProperty(
    val name: AnalyticsUserPropertyName,
    val value: AnalyticsValue
)
