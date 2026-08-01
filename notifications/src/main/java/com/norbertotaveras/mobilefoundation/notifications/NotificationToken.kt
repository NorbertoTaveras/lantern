package com.norbertotaveras.mobilefoundation.notifications

data class NotificationToken(
    val value: String,
    val provider: NotificationTokenProviderType,
    val createdAtMillis: Long? = null,
    val metadata: Map<String, String> = emptyMap()
)
