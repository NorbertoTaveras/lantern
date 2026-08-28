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

package com.norbertotaveras.lantern.auth.firebasegoogle

/**
 * Stable error codes returned by the Firebase + Google auth bridge.
 */
object FirebaseGoogleAuthErrorCodes {
    const val GOOGLE_SIGN_IN_FAILED = "firebase_google_sign_in_failed"
    const val FIREBASE_SIGN_IN_FAILED = "firebase_google_firebase_sign_in_failed"
    const val SIGN_OUT_FAILED = "firebase_google_sign_out_failed"
    const val SESSION_LOOKUP_FAILED = "firebase_google_session_lookup_failed"
    const val SESSION_NOT_FOUND = "firebase_google_session_not_found"
}
