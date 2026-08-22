package com.norbertotaveras.lantern.analytics

/**
 * Analytics event with a validated name and typed parameters.
 */
data class AnalyticsEvent(
    val name: AnalyticsEventName,
    val parameters: Map<String, AnalyticsValue> = emptyMap()
)
