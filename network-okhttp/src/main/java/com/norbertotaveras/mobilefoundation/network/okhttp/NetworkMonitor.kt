package com.norbertotaveras.mobilefoundation.network.okhttp

import kotlinx.coroutines.flow.Flow

interface NetworkMonitor {
    val connectivity: Flow<NetworkConnectivityState>

    fun currentState(): NetworkConnectivityState
}
