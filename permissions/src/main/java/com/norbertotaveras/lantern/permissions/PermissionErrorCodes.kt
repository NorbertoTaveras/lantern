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
 * Stable error codes returned by permission APIs.
 */
object PermissionErrorCodes {
    /**
     * Fallback code for unexpected permission failures.
     */
    const val UNKNOWN = "permission_unknown"
    /**
     * Permission request was attempted without a launcher.
     */
    const val REQUEST_UNAVAILABLE = "permission_request_unavailable"
    /**
     * A required permission was not declared in the app manifest.
     */
    const val PERMISSION_NOT_DECLARED = "permission_not_declared"
    /**
     * The requested SDK permission is not supported on the current platform version.
     */
    const val UNSUPPORTED_PERMISSION = "permission_unsupported"
}
