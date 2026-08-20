package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLink

/**
 * Resolves notification payload deep-link data into a parsed SDK deep link.
 */
interface NotificationDeepLinkResolver {
    /**
     * Returns a parsed deep link, or `null` when [payload] does not contain one.
     */
    fun resolve(payload: NotificationPayload): SdkResult<DeepLink?>
}
