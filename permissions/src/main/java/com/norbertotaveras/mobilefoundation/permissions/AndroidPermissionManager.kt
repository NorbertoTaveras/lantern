package com.norbertotaveras.mobilefoundation.permissions

import android.content.Context
import com.norbertotaveras.mobilefoundation.core.SdkError
import com.norbertotaveras.mobilefoundation.permissions.internal.AndroidVersionPermissionResolver
import com.norbertotaveras.mobilefoundation.permissions.internal.PermissionChecker
import com.norbertotaveras.mobilefoundation.permissions.internal.PermissionMapper
import com.norbertotaveras.mobilefoundation.permissions.internal.ResolvedPermission

class AndroidPermissionManager private constructor(
    context: Context,
    private val requestLauncher: PermissionRequestLauncher? = null,
    private val rationaleProvider: PermissionRationaleProvider = PermissionRationaleProvider { false },
    private val resolver: AndroidVersionPermissionResolver = AndroidVersionPermissionResolver()
) : PermissionManager {

    constructor(
        context: Context,
        requestLauncher: PermissionRequestLauncher? = null,
        rationaleProvider: PermissionRationaleProvider = PermissionRationaleProvider { false }
    ) : this(
        context = context,
        requestLauncher = requestLauncher,
        rationaleProvider = rationaleProvider,
        resolver = AndroidVersionPermissionResolver()
    )

    private val checker = PermissionChecker(context.applicationContext)
    private val mapper = PermissionMapper(checker, rationaleProvider)

    override fun check(permission: SdkPermission): PermissionState {
        return mapper.toState(resolver.resolve(permission))
    }

    override fun checkMultiple(permissions: List<SdkPermission>): Map<SdkPermission, PermissionState> {
        return normalizePermissions(permissions).associateWith { check(it) }
    }

    override suspend fun request(permission: SdkPermission): PermissionResult {
        return requestMultiple(listOf(permission))
    }

    override suspend fun requestMultiple(permissions: List<SdkPermission>): PermissionResult {
        val resolutions = normalizePermissions(permissions).map(resolver::resolve)
        val requestablePermissions = requestableManifestPermissions(resolutions)

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

    private fun normalizePermissions(permissions: List<SdkPermission>): List<SdkPermission> {
        return permissions.distinct()
    }

    private fun requestableManifestPermissions(resolutions: List<ResolvedPermission>): List<String> {
        return resolutions
            .flatMap { it.manifestPermissions }
            .distinct()
    }
}
