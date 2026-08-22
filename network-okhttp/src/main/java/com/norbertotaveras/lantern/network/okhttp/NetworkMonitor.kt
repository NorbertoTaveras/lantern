package com.norbertotaveras.lantern.network.okhttp

import kotlinx.coroutines.flow.Flow

/**
 * Observes device connectivity independently from any specific HTTP client.
 */
interface NetworkMonitor {
    /**
     * Stream of connectivity changes.
     */
    val connectivity: Flow<NetworkConnectivityState>

    /**
     * Returns the latest known connectivity state synchronously.
     */
    fun currentState(): NetworkConnectivityState
}
