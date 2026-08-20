package com.norbertotaveras.mobilefoundation.deeplinks

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.deeplinks.internal.DeepLinkUriParser

/**
 * Default [DeepLinkParser] implementation with optional scheme and host allow-lists.
 */
class DefaultDeepLinkParser(
    private val config: DeepLinkConfig = DeepLinkConfig.AllowAny
) : DeepLinkParser {
    private val uriParser = DeepLinkUriParser()

    /**
     * Parses and validates [value] using [DeepLinkConfig].
     */
    override fun parse(value: String): SdkResult<DeepLink> {
        return when (val result = uriParser.parse(value)) {
            is SdkResult.Failure -> result
            is SdkResult.Success -> validate(result.data)
        }
    }

    private fun validate(deepLink: DeepLink): SdkResult<DeepLink> {
        if (!config.allowsScheme(deepLink.scheme)) {
            return SdkResult.Failure(
                SdkError(
                    code = DeepLinkErrorCodes.INVALID_SCHEME,
                    message = "Deep link scheme '${deepLink.scheme}' is not allowed."
                )
            )
        }

        if (!config.allowsHost(deepLink.host)) {
            return SdkResult.Failure(
                SdkError(
                    code = DeepLinkErrorCodes.INVALID_HOST,
                    message = "Deep link host '${deepLink.host.orEmpty()}' is not allowed."
                )
            )
        }

        return SdkResult.Success(deepLink)
    }
}
