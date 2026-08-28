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
 * Network transport categories reported by [NetworkMonitor].
 */
enum class NetworkTransport {
    /**
     * Wi-Fi network transport.
     */
    Wifi,
    /**
     * Cellular network transport.
     */
    Cellular,
    /**
     * Ethernet network transport.
     */
    Ethernet,
    /**
     * Bluetooth network transport.
     */
    Bluetooth,
    /**
     * VPN network transport.
     */
    Vpn,
    /**
     * Transport is unknown or not modeled by the SDK.
     */
    Other
}
