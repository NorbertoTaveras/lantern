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

package com.norbertotaveras.lantern.permissions

import com.norbertotaveras.lantern.core.SdkError
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Test

class PermissionResultTest {

    @Test
    fun `allGranted is true when every state is granted`() {
        val result = PermissionResult(
            states = mapOf(
                SdkPermission.Camera to PermissionState(
                    permission = SdkPermission.Camera,
                    status = PermissionStatus.Granted
                ),
                SdkPermission.Microphone to PermissionState(
                    permission = SdkPermission.Microphone,
                    status = PermissionStatus.Granted
                )
            )
        )

        assertTrue(result.allGranted)
    }

    @Test
    fun `allGranted is false when any state is not granted`() {
        val result = PermissionResult(
            states = mapOf(
                SdkPermission.Camera to PermissionState(
                    permission = SdkPermission.Camera,
                    status = PermissionStatus.Granted
                ),
                SdkPermission.Microphone to PermissionState(
                    permission = SdkPermission.Microphone,
                    status = PermissionStatus.Denied
                )
            )
        )

        assertFalse(result.allGranted)
    }

    @Test
    fun `single creates a one permission result and keeps error`() {
        val state = PermissionState(
            permission = SdkPermission.Camera,
            status = PermissionStatus.Denied
        )
        val error = SdkError(
            code = PermissionErrorCodes.REQUEST_UNAVAILABLE,
            message = "No launcher"
        )

        val result = PermissionResult.single(state, error)

        assertEquals(mapOf(SdkPermission.Camera to state), result.states)
        assertSame(error, result.error)
    }
}
