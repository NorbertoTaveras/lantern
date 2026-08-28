/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.norbertotaveras.lantern.deeplinks

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.internal.DeepLinkUriParser

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
