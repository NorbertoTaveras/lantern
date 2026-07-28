package com.norbertotaveras.mobilefoundation.permissions.internal

import android.Manifest
import android.os.Build
import com.norbertotaveras.mobilefoundation.permissions.PermissionStatus
import com.norbertotaveras.mobilefoundation.permissions.SdkPermission
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AndroidVersionPermissionResolverTest {

    @Test
    fun `resolves common runtime permissions to manifest permissions`() {
        val resolver = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.UPSIDE_DOWN_CAKE)

        assertRequires(
            resolver.resolve(SdkPermission.Camera),
            Manifest.permission.CAMERA
        )
        assertRequires(
            resolver.resolve(SdkPermission.FineLocation),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        assertRequires(
            resolver.resolve(SdkPermission.CoarseLocation),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        assertRequires(
            resolver.resolve(SdkPermission.Microphone),
            Manifest.permission.RECORD_AUDIO
        )
        assertRequires(
            resolver.resolve(SdkPermission.Contacts),
            Manifest.permission.READ_CONTACTS
        )
    }

    @Test
    fun `background location is granted before Android 10 and runtime on Android 10 plus`() {
        val preAndroid10 = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.P)
            .resolve(SdkPermission.BackgroundLocation)
        val android10 = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.Q)
            .resolve(SdkPermission.BackgroundLocation)

        assertEquals(emptyList<String>(), preAndroid10.manifestPermissions)
        assertEquals(PermissionStatus.Granted, preAndroid10.fixedStatus)
        assertRequires(android10, Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    @Test
    fun `notifications are granted before Android 13 and runtime on Android 13 plus`() {
        val preAndroid13 = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.S_V2)
            .resolve(SdkPermission.Notifications)
        val android13 = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.TIRAMISU)
            .resolve(SdkPermission.Notifications)

        assertEquals(emptyList<String>(), preAndroid13.manifestPermissions)
        assertEquals(PermissionStatus.Granted, preAndroid13.fixedStatus)
        assertRequires(android13, Manifest.permission.POST_NOTIFICATIONS)
    }

    @Test
    fun `bluetooth permissions are granted before Android 12 and runtime on Android 12 plus`() {
        val preAndroid12Resolver = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.R)
        val android12Resolver = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.S)

        listOf(
            SdkPermission.BluetoothScan,
            SdkPermission.BluetoothConnect,
            SdkPermission.BluetoothAdvertise
        ).forEach { permission ->
            val preAndroid12 = preAndroid12Resolver.resolve(permission)

            assertEquals(emptyList<String>(), preAndroid12.manifestPermissions)
            assertEquals(PermissionStatus.Granted, preAndroid12.fixedStatus)
        }

        assertRequires(
            android12Resolver.resolve(SdkPermission.BluetoothScan),
            Manifest.permission.BLUETOOTH_SCAN
        )
        assertRequires(
            android12Resolver.resolve(SdkPermission.BluetoothConnect),
            Manifest.permission.BLUETOOTH_CONNECT
        )
        assertRequires(
            android12Resolver.resolve(SdkPermission.BluetoothAdvertise),
            Manifest.permission.BLUETOOTH_ADVERTISE
        )
    }

    @Test
    fun `media read permissions use external storage before Android 13`() {
        val resolver = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.S_V2)

        listOf(
            SdkPermission.ReadMediaImages,
            SdkPermission.ReadMediaVideo,
            SdkPermission.ReadMediaAudio
        ).forEach { permission ->
            assertRequires(
                resolver.resolve(permission),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    @Test
    fun `media read permissions use granular media permissions on Android 13 plus`() {
        val resolver = AndroidVersionPermissionResolver(sdkInt = Build.VERSION_CODES.TIRAMISU)

        assertRequires(
            resolver.resolve(SdkPermission.ReadMediaImages),
            Manifest.permission.READ_MEDIA_IMAGES
        )
        assertRequires(
            resolver.resolve(SdkPermission.ReadMediaVideo),
            Manifest.permission.READ_MEDIA_VIDEO
        )
        assertRequires(
            resolver.resolve(SdkPermission.ReadMediaAudio),
            Manifest.permission.READ_MEDIA_AUDIO
        )
    }

    private fun assertRequires(
        resolvedPermission: ResolvedPermission,
        manifestPermission: String
    ) {
        assertEquals(listOf(manifestPermission), resolvedPermission.manifestPermissions)
        assertNull(resolvedPermission.fixedStatus)
    }
}
