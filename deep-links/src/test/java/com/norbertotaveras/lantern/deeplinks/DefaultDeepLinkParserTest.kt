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

import com.norbertotaveras.lantern.core.SdkResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultDeepLinkParserTest {

    @Test
    fun parseReturnsDeepLinkForAllowedUri() {
        val parser = DefaultDeepLinkParser(
            DeepLinkConfig(
                allowedSchemes = setOf("lantern"),
                allowedHosts = setOf("feature")
            )
        )

        val result = parser.parse("lantern://feature/auth/firebase?source=drawer&source=home#details")

        assertTrue(result is SdkResult.Success)
        val deepLink = (result as SdkResult.Success).data
        assertEquals("lantern", deepLink.scheme)
        assertEquals("feature", deepLink.host)
        assertEquals(listOf("auth", "firebase"), deepLink.pathSegments)
        assertEquals(listOf("drawer", "home"), deepLink.queryParameters["source"])
        assertEquals("drawer", deepLink.firstQueryParameter("source"))
        assertEquals("details", deepLink.fragment)
    }

    @Test
    fun parseRejectsDisallowedScheme() {
        val parser = DefaultDeepLinkParser(
            DeepLinkConfig(allowedSchemes = setOf("lantern"))
        )

        val result = parser.parse("https://example.com/auth")

        assertTrue(result is SdkResult.Failure)
        assertEquals(DeepLinkErrorCodes.INVALID_SCHEME, (result as SdkResult.Failure).error.code)
    }

    @Test
    fun parseRejectsDisallowedHost() {
        val parser = DefaultDeepLinkParser(
            DeepLinkConfig(
                allowedSchemes = setOf("lantern"),
                allowedHosts = setOf("feature")
            )
        )

        val result = parser.parse("lantern://settings/auth")

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
