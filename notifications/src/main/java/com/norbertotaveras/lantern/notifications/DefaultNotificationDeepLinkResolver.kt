package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLink
import com.norbertotaveras.lantern.deeplinks.DeepLinkParser
import com.norbertotaveras.lantern.deeplinks.DefaultDeepLinkParser

/**
 * Default resolver that parses [NotificationPayload.deepLink] with the SDK deep-link parser.
 */
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
