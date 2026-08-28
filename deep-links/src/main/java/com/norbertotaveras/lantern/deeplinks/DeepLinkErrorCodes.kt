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

package com.norbertotaveras.lantern.deeplinks

/**
 * Stable error codes returned by deep-link APIs.
 */
object DeepLinkErrorCodes {
    const val INVALID_URI = "deep_link_invalid_uri"
    const val INVALID_SCHEME = "deep_link_invalid_scheme"
    const val INVALID_HOST = "deep_link_invalid_host"
    const val MISSING_INTENT_URI = "deep_link_missing_intent_uri"
}
