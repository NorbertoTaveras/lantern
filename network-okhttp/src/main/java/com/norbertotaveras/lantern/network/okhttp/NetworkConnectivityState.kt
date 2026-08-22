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
