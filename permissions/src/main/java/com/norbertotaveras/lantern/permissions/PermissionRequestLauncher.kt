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
 * App-provided bridge that launches Android permission UI.
 *
 * The SDK stays UI-independent, so apps should implement this with Activity Result APIs or another
 * lifecycle-aware permission launcher. Return a map from each requested Android manifest permission to
 * whether it was granted.
 */
fun interface PermissionRequestLauncher {
    /**
     * Requests [manifestPermissions] and returns grant results.
     *
     * Throwing a non-cancellation exception is converted into a [PermissionResult.error] by
     * [PermissionManager] implementations. Coroutine cancellation should be allowed to propagate.
     */
    suspend fun request(manifestPermissions: List<String>): Map<String, Boolean>
}
