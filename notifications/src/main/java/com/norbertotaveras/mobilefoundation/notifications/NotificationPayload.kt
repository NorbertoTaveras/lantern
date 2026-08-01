package com.norbertotaveras.mobilefoundation.notifications

data class NotificationPayload(
    val title: String? = null,
    val body: String? = null,
    val deepLink: NotificationDeepLink? = null,
    val data: Map<String, String> = emptyMap()
)
