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

package com.norbertotaveras.lantern.remoteconfig.firebase

import com.norbertotaveras.lantern.remoteconfig.firebase.internal.FirebaseRemoteConfigErrorMapper
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test

class FirebaseRemoteConfigErrorMapperTest {

    @Test
    fun mapUsesOperationErrorCodeFallbackMessageAndCause() {
        val throwable = IllegalStateException("Fetch failed")

        val error = FirebaseRemoteConfigErrorMapper().map(
            operation = FirebaseRemoteConfigErrorMapper.Operation.Fetch,
            throwable = throwable
        )

        assertEquals(FirebaseRemoteConfigErrorCodes.FETCH_FAILED, error.code)
        assertEquals("Unable to fetch Firebase Remote Config values.", error.message)
        assertSame(throwable, error.cause)
    }
}
