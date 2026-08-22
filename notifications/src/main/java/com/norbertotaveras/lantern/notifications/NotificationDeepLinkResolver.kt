package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLink

/**
 * Resolves notification payload deep-link data into a parsed SDK deep link.
 */
interface NotificationDeepLinkResolver {
    /**
     * Returns a parsed deep link, or `null` when [payload] does not contain one.
     */
    fun resolve(payload: NotificationPayload): SdkResult<DeepLink?>
}
