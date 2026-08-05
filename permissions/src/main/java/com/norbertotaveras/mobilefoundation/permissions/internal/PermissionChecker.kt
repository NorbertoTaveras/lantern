package com.norbertotaveras.mobilefoundation.permissions.internal

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

internal class PermissionChecker(
    private val context: Context
) {

    fun isGranted(manifestPermission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            manifestPermission
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isDeclared(manifestPermission: String): Boolean {
        return requestedPermissions().contains(manifestPermission)
    }

    @Suppress("DEPRECATION")
    private fun requestedPermissions(): Set<String> {
        return try {
            context.packageManager
                .getPackageInfo(context.packageName, PackageManager.GET_PERMISSIONS)
                .requestedPermissions
                ?.toSet()
                .orEmpty()
        } catch (_: PackageManager.NameNotFoundException) {
            emptySet()
        }
    }
}
