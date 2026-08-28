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

/**
 * Checks and requests Android runtime permissions using SDK-level permission types.
 *
 * Implementations return one state per [SdkPermission]. Duplicate permissions passed to multi-permission
 * methods are treated as one logical request.
 */
interface PermissionManager {
    /**
     * Returns the current state for [permission] without showing Android permission UI.
     */
    fun check(permission: SdkPermission): PermissionState

    /**
     * Returns current states for [permissions] without showing Android permission UI.
     */
    fun checkMultiple(permissions: List<SdkPermission>): Map<SdkPermission, PermissionState>

    /**
     * Requests [permission] and returns the resulting state.
     *
     * Implementations should preserve coroutine cancellation.
     */
    suspend fun request(permission: SdkPermission): PermissionResult

    /**
     * Requests [permissions] and returns the resulting states.
     *
     * Underlying manifest permissions may be deduplicated before launching Android permission UI.
     */
    suspend fun requestMultiple(permissions: List<SdkPermission>): PermissionResult
}
