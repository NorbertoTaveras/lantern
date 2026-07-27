package com.norbertotaveras.mobilefoundation.permissions.internal

import com.norbertotaveras.mobilefoundation.permissions.PermissionRationaleProvider
import com.norbertotaveras.mobilefoundation.permissions.PermissionState
import com.norbertotaveras.mobilefoundation.permissions.PermissionStatus

class PermissionMapper(
    private val checker: PermissionChecker,
    private val rationaleProvider: PermissionRationaleProvider
) {

    fun toState(
        resolution: ResolvedPermission,
        grantResults: Map<String, Boolean>? = null
    ): PermissionState {
        val fixedStatus = resolution.fixedStatus
        if (fixedStatus != null) {
            return PermissionState(
                permission = resolution.permission,
                status = fixedStatus
            )
        }

        if (resolution.manifestPermissions.isEmpty()) {
            return PermissionState(
                permission = resolution.permission,
                status = PermissionStatus.Unsupported
            )
        }

        val shouldShowRationale = resolution.manifestPermissions.any(
            rationaleProvider::shouldShowRationale
        )

        val allDeclared = resolution.manifestPermissions.all(checker::isDeclared)
        if (!allDeclared) {
            return PermissionState(
                permission = resolution.permission,
                status = PermissionStatus.Denied,
                shouldShowRationale = shouldShowRationale
            )
        }

        val allGranted = resolution.manifestPermissions.all { manifestPermission ->
            grantResults?.get(manifestPermission) ?: checker.isGranted(manifestPermission)
        }

        return PermissionState(
            permission = resolution.permission,
            status = if (allGranted) PermissionStatus.Granted else PermissionStatus.Denied,
            shouldShowRationale = shouldShowRationale
        )
    }
}
