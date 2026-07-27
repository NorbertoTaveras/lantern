package com.norbertotaveras.mobilefoundation.permissions.internal

import android.Manifest
import android.os.Build
import com.norbertotaveras.mobilefoundation.permissions.PermissionStatus
import com.norbertotaveras.mobilefoundation.permissions.SdkPermission

class AndroidVersionPermissionResolver(
    private val sdkInt: Int = Build.VERSION.SDK_INT
) {

    fun resolve(permission: SdkPermission): ResolvedPermission {
        return when (permission) {
            SdkPermission.Camera -> permission.requires(Manifest.permission.CAMERA)
            SdkPermission.FineLocation -> permission.requires(Manifest.permission.ACCESS_FINE_LOCATION)
            SdkPermission.CoarseLocation -> permission.requires(Manifest.permission.ACCESS_COARSE_LOCATION)
            SdkPermission.BackgroundLocation -> resolveBackgroundLocation(permission)
            SdkPermission.Microphone -> permission.requires(Manifest.permission.RECORD_AUDIO)
            SdkPermission.Notifications -> resolveNotifications(permission)
            SdkPermission.BluetoothScan -> resolveBluetooth(permission, Manifest.permission.BLUETOOTH_SCAN)
            SdkPermission.BluetoothConnect -> resolveBluetooth(permission, Manifest.permission.BLUETOOTH_CONNECT)
            SdkPermission.BluetoothAdvertise -> resolveBluetooth(permission, Manifest.permission.BLUETOOTH_ADVERTISE)
            SdkPermission.Contacts -> permission.requires(Manifest.permission.READ_CONTACTS)
            SdkPermission.ReadMediaImages -> resolveMediaRead(permission, Manifest.permission.READ_MEDIA_IMAGES)
            SdkPermission.ReadMediaVideo -> resolveMediaRead(permission, Manifest.permission.READ_MEDIA_VIDEO)
            SdkPermission.ReadMediaAudio -> resolveMediaRead(permission, Manifest.permission.READ_MEDIA_AUDIO)
        }
    }

    private fun resolveBackgroundLocation(permission: SdkPermission): ResolvedPermission {
        return if (sdkInt >= Build.VERSION_CODES.Q) {
            permission.requires(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        } else {
            permission.noRuntimePermissionRequired()
        }
    }

    private fun resolveNotifications(permission: SdkPermission): ResolvedPermission {
        return if (sdkInt >= Build.VERSION_CODES.TIRAMISU) {
            permission.requires(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            permission.noRuntimePermissionRequired()
        }
    }

    private fun resolveBluetooth(
        permission: SdkPermission,
        manifestPermission: String
    ): ResolvedPermission {
        return if (sdkInt >= Build.VERSION_CODES.S) {
            permission.requires(manifestPermission)
        } else {
            permission.noRuntimePermissionRequired()
        }
    }

    private fun resolveMediaRead(
        permission: SdkPermission,
        android13Permission: String
    ): ResolvedPermission {
        return if (sdkInt >= Build.VERSION_CODES.TIRAMISU) {
            permission.requires(android13Permission)
        } else {
            permission.requires(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun SdkPermission.requires(vararg permissions: String): ResolvedPermission {
        return ResolvedPermission(
            permission = this,
            manifestPermissions = permissions.toList()
        )
    }

    private fun SdkPermission.noRuntimePermissionRequired(): ResolvedPermission {
        return ResolvedPermission(
            permission = this,
            manifestPermissions = emptyList(),
            fixedStatus = PermissionStatus.Granted
        )
    }
}
