package com.norbertotaveras.mobilefoundation.deeplinks

import com.norbertotaveras.mobilefoundation.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultDeepLinkParserTest {

    @Test
    fun parseReturnsDeepLinkForAllowedUri() {
        val parser = DefaultDeepLinkParser(
            DeepLinkConfig(
                allowedSchemes = setOf("mobilefoundation"),
                allowedHosts = setOf("feature")
            )
        )

        val result = parser.parse("mobilefoundation://feature/auth/firebase?source=drawer&source=home#details")

        assertTrue(result is SdkResult.Success)
        val deepLink = (result as SdkResult.Success).data
        assertEquals("mobilefoundation", deepLink.scheme)
        assertEquals("feature", deepLink.host)
        assertEquals(listOf("auth", "firebase"), deepLink.pathSegments)
        assertEquals(listOf("drawer", "home"), deepLink.queryParameters["source"])
        assertEquals("drawer", deepLink.firstQueryParameter("source"))
        assertEquals("details", deepLink.fragment)
    }

    @Test
    fun parseRejectsDisallowedScheme() {
        val parser = DefaultDeepLinkParser(
            DeepLinkConfig(allowedSchemes = setOf("mobilefoundation"))
        )

        val result = parser.parse("https://example.com/auth")

        assertTrue(result is SdkResult.Failure)
        assertEquals(DeepLinkErrorCodes.INVALID_SCHEME, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun parseRejectsDisallowedHost() {
        val parser = DefaultDeepLinkParser(
            DeepLinkConfig(
                allowedSchemes = setOf("mobilefoundation"),
                allowedHosts = setOf("feature")
            )
        )

        val result = parser.parse("mobilefoundation://settings/auth")

        assertTrue(result is SdkResult.Failure)
        assertEquals(DeepLinkErrorCodes.INVALID_HOST, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun parseRejectsBlankUri() {
        val result = DefaultDeepLinkParser().parse(" ")

        assertTrue(result is SdkResult.Failure)
        assertEquals(DeepLinkErrorCodes.INVALID_URI, (result as SdkResult.Failure).error.code)
    }
}
