package com.norbertotaveras.mobilefoundation.deeplinks.internal

import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.core.SdkResult
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLink
import com.norbertotaveras.mobilefoundation.deeplinks.DeepLinkErrorCodes
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
