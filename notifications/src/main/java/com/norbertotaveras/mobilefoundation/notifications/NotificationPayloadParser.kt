package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult

/**
 * Parses provider-specific notification payload data into [NotificationPayload].
 */
interface NotificationPayloadParser {
    /**
     * Parses raw notification [data].
     */
    fun parse(data: Map<String, String>): SdkResult<NotificationPayload>
}
