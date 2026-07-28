package com.norbertotaveras.mobilefoundation.permissions

import com.norbertotaveras.mobilefoundation.core.SdkError
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
