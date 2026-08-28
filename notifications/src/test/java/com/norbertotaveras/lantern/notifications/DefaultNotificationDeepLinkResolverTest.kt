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

package com.norbertotaveras.lantern.notifications

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.deeplinks.DeepLinkErrorCodes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DefaultNotificationDeepLinkResolverTest {
    private val resolver = DefaultNotificationDeepLinkResolver()

    @Test
    fun resolveReturnsNullWhenPayloadHasNoDeepLink() {
        val result = resolver.resolve(NotificationPayload(title = "Hello"))

        assertTrue(result is SdkResult.Success)
        assertEquals(null, (result as SdkResult.Success).data)
    }

    @Test
    fun resolveParsesNotificationDeepLinkUri() {
        val result = resolver.resolve(
            NotificationPayload(
                deepLink = NotificationDeepLink(uri = "lantern://feature/auth?source=push")
            )
        )

        assertTrue(result is SdkResult.Success)
        val deepLink = (result as SdkResult.Success).data
        assertEquals("lantern", deepLink?.scheme)
        assertEquals("feature", deepLink?.host)
        assertEquals(listOf("auth"), deepLink?.pathSegments)
        assertEquals("push", deepLink?.firstQueryParameter("source"))
    }

    @Test
    fun resolveReturnsParserFailureForInvalidUri() {
        val result = resolver.resolve(
            NotificationPayload(deepLink = NotificationDeepLink(uri = " "))
        )

        assertTrue(result is SdkResult.Failure)
        assertEquals(DeepLinkErrorCodes.INVALID_URI, (result as SdkResult.Failure).error.code)
    }
}
