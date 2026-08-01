package com.norbertotaveras.mobilefoundation.notifications

data class NotificationChannelConfig(
    val id: NotificationChannelId,
    val name: String,
    val description: String? = null,
    val importance: NotificationChannelImportance = NotificationChannelImportance.Default,
    val showBadge: Boolean = true,
    val metadata: Map<String, String> = emptyMap()
)
