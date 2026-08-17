package com.norbertotaveras.mobilefoundation.analytics

data class AnalyticsEvent(
    val name: AnalyticsEventName,
    val parameters: Map<String, AnalyticsValue> = emptyMap()
)
