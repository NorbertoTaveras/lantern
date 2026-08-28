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
 * Current SDK-level state for a permission.
 *
 * [shouldShowRationale] mirrors Android rationale guidance for the resolved manifest permissions.
 */
data class PermissionState(
    val permission: SdkPermission,
    val status: PermissionStatus,
    val shouldShowRationale: Boolean = false
) {
    /**
     * True when [status] is [PermissionStatus.Granted].
     */
    val isGranted: Boolean = status == PermissionStatus.Granted
}
