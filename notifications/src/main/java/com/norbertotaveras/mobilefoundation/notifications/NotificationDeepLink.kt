package com.norbertotaveras.mobilefoundation.notifications

data class NotificationDeepLink(
    val uri: String,
    val route: String? = null,
    val parameters: Map<String, String> = emptyMap()
)
