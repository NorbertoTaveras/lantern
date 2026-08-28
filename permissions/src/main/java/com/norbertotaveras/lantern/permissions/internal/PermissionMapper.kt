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

package com.norbertotaveras.lantern.permissions.internal

import com.norbertotaveras.lantern.permissions.PermissionRationaleProvider
import com.norbertotaveras.lantern.permissions.PermissionState
import com.norbertotaveras.lantern.permissions.PermissionStatus

internal class PermissionMapper private constructor(
    private val isGranted: (String) -> Boolean,
    private val isDeclared: (String) -> Boolean,
    private val rationaleProvider: PermissionRationaleProvider
) {

    constructor(
        checker: PermissionChecker,
        rationaleProvider: PermissionRationaleProvider
    ) : this(
        isGranted = checker::isGranted,
        isDeclared = checker::isDeclared,
        rationaleProvider = rationaleProvider
    )

    constructor(
        isGranted: (String) -> Boolean,
        isDeclared: (String) -> Boolean,
        shouldShowRationale: (String) -> Boolean = { false }
    ) : this(
        isGranted = isGranted,
        isDeclared = isDeclared,
        rationaleProvider = PermissionRationaleProvider(shouldShowRationale)
    )

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

        val allDeclared = resolution.manifestPermissions.all(isDeclared)
        if (!allDeclared) {
            return PermissionState(
                permission = resolution.permission,
                status = PermissionStatus.Denied,
                shouldShowRationale = shouldShowRationale
            )
        }

        val deniedPermissions = resolution.manifestPermissions.filter { manifestPermission ->
            !(grantResults?.get(manifestPermission) ?: isGranted(manifestPermission))
        }
        val status = when {
            deniedPermissions.isEmpty() -> PermissionStatus.Granted
            grantResults != null && deniedPermissions.any { manifestPermission ->
                grantResults[manifestPermission] == false && !rationaleProvider.shouldShowRationale(manifestPermission)
            } ->
                PermissionStatus.PermanentlyDenied
            else -> PermissionStatus.Denied
        }

        return PermissionState(
            permission = resolution.permission,
            status = status,
            shouldShowRationale = shouldShowRationale
        )
    }
}
