package com.norbertotaveras.mobilefoundation.permissions

import android.content.Context
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.permissions.internal.AndroidVersionPermissionResolver
import com.norbertotaveras.mobilefoundation.permissions.internal.PermissionChecker
import com.norbertotaveras.mobilefoundation.permissions.internal.PermissionMapper

class AndroidPermissionManager(
    context: Context,
    private val requestLauncher: PermissionRequestLauncher? = null,
    private val rationaleProvider: PermissionRationaleProvider = PermissionRationaleProvider { false },
    private val resolver: AndroidVersionPermissionResolver = AndroidVersionPermissionResolver()
) : PermissionManager {

    private val checker = PermissionChecker(context.applicationContext)
    private val mapper = PermissionMapper(checker, rationaleProvider)

    override fun check(permission: SdkPermission): PermissionState {
        return mapper.toState(resolver.resolve(permission))
    }

    override fun checkMultiple(permissions: List<SdkPermission>): Map<SdkPermission, PermissionState> {
        return permissions.associateWith { check(it) }
    }

    override suspend fun request(permission: SdkPermission): PermissionResult {
        return requestMultiple(listOf(permission))
    }

    override suspend fun requestMultiple(permissions: List<SdkPermission>): PermissionResult {
        val resolutions = permissions.map(resolver::resolve)
        val requestablePermissions = resolutions
            .flatMap { it.manifestPermissions }
            .distinct()

        if (requestablePermissions.isEmpty()) {
            return PermissionResult(
                states = resolutions.associate { it.permission to mapper.toState(it) }
            )
        }

        val launcher = requestLauncher
        if (launcher == null) {
            return PermissionResult(
                states = resolutions.associate { it.permission to mapper.toState(it) },
                error = SdkError(
                    code = PermissionErrorCodes.REQUEST_UNAVAILABLE,
                    message = "Permission requests require a PermissionRequestLauncher."
                )
            )
        }

        val grantResults = launcher.request(requestablePermissions)

        return PermissionResult(
            states = resolutions.associate { resolution ->
                resolution.permission to mapper.toState(
                    resolution = resolution,
                    grantResults = grantResults
                )
            }
        )
    }
}
