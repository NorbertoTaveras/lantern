package com.norbertotaveras.mobilefoundation.network.okhttp

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
