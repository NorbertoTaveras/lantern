package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

interface NotificationPayloadParser {
    fun parse(data: Map<String, String>): SdkResult<NotificationPayload>
}
