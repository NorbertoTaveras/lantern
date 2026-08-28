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

package com.norbertotaveras.lantern.network.okhttp

/**
 * Supplies access tokens for network requests without coupling the network module to an auth provider.
 */
interface TokenProvider {
    /**
     * Returns the current access token, or `null` when the request should be sent unauthenticated.
     *
     * Blank tokens are ignored by [AuthHeaderInterceptor].
     */
    fun getAccessToken(): String?
}
