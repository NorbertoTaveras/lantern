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
 * Snapshot of the active network state.
 */
data class NetworkConnectivityState(
    /**
     * Whether a network is currently available.
     */
    val isAvailable: Boolean,
    /**
     * Whether Android has validated that the network can reach the internet.
     */
    val isValidated: Boolean = false,
    /**
     * Known transports for the active network.
     */
    val transports: Set<NetworkTransport> = emptySet()
) {
    /**
     * True when a network is available and validated.
     */
    val isUsable: Boolean
        get() = isAvailable && isValidated

    companion object {
        /**
         * Default state used when no usable network is known.
         */
        val Unavailable = NetworkConnectivityState(isAvailable = false)
    }
}
