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

package com.norbertotaveras.lantern.deeplinks.internal

import com.norbertotaveras.lantern.core.SdkError
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLink
import com.norbertotaveras.lantern.deeplinks.DeepLinkErrorCodes
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

internal class DeepLinkUriParser {
    fun parse(value: String): SdkResult<DeepLink> {
        val normalizedValue = value.trim()
        if (normalizedValue.isEmpty()) {
            return invalidUri("Deep link URI cannot be blank.")
        }

        val uri = try {
            URI(normalizedValue)
        } catch (_: IllegalArgumentException) {
            return invalidUri("Deep link URI is invalid.")
        }

        val scheme = uri.scheme
        if (scheme.isNullOrBlank()) {
            return invalidUri("Deep link URI must include a scheme.")
        }

        return SdkResult.Success(
            DeepLink(
                rawValue = normalizedValue,
                scheme = scheme,
                host = uri.host,
                pathSegments = uri.rawPath.toPathSegments(),
                queryParameters = uri.rawQuery.toQueryParameters(),
                fragment = uri.rawFragment?.decode()
            )
        )
    }

    private fun String?.toPathSegments(): List<String> {
        return this?.split("/")
            ?.filter { it.isNotBlank() }
            ?.map { it.decode() }
            .orEmpty()
    }

    private fun String?.toQueryParameters(): Map<String, List<String>> {
        if (isNullOrBlank()) {
            return emptyMap()
        }

        return split("&")
            .filter { it.isNotBlank() }
            .map { parameter ->
                val key = parameter.substringBefore("=").decode()
                val value = parameter.substringAfter("=", missingDelimiterValue = "").decode()
                key to value
            }
            .groupBy(
                keySelector = { it.first },
                valueTransform = { it.second }
            )
    }

    private fun String.decode(): String {
        return URLDecoder.decode(this, StandardCharsets.UTF_8.name())
    }

    private fun invalidUri(message: String): SdkResult.Failure {
        return SdkResult.Failure(
            SdkError(
                code = DeepLinkErrorCodes.INVALID_URI,
                message = message
            )
        )
    }
}
