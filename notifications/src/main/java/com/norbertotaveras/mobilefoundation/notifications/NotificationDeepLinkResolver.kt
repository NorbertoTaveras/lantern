package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLink

interface NotificationDeepLinkResolver {
    fun resolve(payload: NotificationPayload): SdkResult<DeepLink?>
}
