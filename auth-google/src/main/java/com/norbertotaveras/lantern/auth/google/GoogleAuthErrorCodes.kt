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

package com.norbertotaveras.lantern.auth.google

/**
 * Stable error codes returned by Google auth integration.
 */
object GoogleAuthErrorCodes {
    const val UNKNOWN = "google_auth_unknown"
    const val USER_CANCELLED = "google_auth_user_cancelled"
    const val NO_CREDENTIAL = "google_auth_no_credential"
    const val INVALID_CREDENTIAL = "google_auth_invalid_credential"
    const val CLEAR_CREDENTIAL_FAILED = "google_auth_clear_credential_failed"
}
