package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult

/**
 * Parses provider-specific notification payload data into [NotificationPayload].
 */
interface NotificationPayloadParser {
    /**
     * Parses raw notification [data].
     */
    fun parse(data: Map<String, String>): SdkResult<NotificationPayload>
}
