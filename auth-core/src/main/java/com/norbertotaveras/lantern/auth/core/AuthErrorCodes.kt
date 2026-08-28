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

package com.norbertotaveras.lantern.auth.core

/**
 * Stable provider-neutral error codes returned by auth APIs.
 */
object AuthErrorCodes {
    const val UNKNOWN = "auth_unknown"
    const val USER_CANCELLED = "auth_user_cancelled"
    const val SESSION_NOT_FOUND = "auth_session_not_found"
    const val INVALID_CREDENTIALS = "auth_invalid_credentials"
    const val PROVIDER_UNAVAILABLE = "auth_provider_unavailable"
}
