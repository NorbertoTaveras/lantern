package com.norbertotaveras.mobilefoundation.notifications

import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLink
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLinkParser
import com.norbertotaveras.mobilefoundation.deeplinks.DefaultDeepLinkParser

class DefaultNotificationDeepLinkResolver(
    private val parser: DeepLinkParser = DefaultDeepLinkParser()
) : NotificationDeepLinkResolver {
    override fun resolve(payload: NotificationPayload): SdkResult<DeepLink?> {
        val deepLink = payload.deepLink ?: return SdkResult.Success(null)

        return when (val result = parser.parse(deepLink.uri)) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> SdkResult.Success(result.data)
        }
    }
}
