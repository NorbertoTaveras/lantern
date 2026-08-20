package com.norbertotaveras.mobilefoundation.network.okhttp.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.norbertotaveras.mobilefoundation.network.okhttp.NetworkConnectivityState
import com.norbertotaveras.mobilefoundation.network.okhttp.NetworkMonitor
import com.norbertotaveras.mobilefoundation.network.okhttp.NetworkTransport
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

/**
 * Android [NetworkMonitor] implementation backed by [ConnectivityManager].
 */
class AndroidNetworkMonitor(
    context: Context
) : NetworkMonitor {

    private val connectivityManager =
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    override val connectivity: Flow<NetworkConnectivityState> =
        connectivityManager.connectivityStateChanges()

    override fun currentState(): NetworkConnectivityState {
        return connectivityManager.currentConnectivityState()
    }
}

private fun ConnectivityManager.connectivityStateChanges(): Flow<NetworkConnectivityState> {
    return callbackFlow {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(currentConnectivityState())
            }

            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                trySend(networkCapabilities.toNetworkConnectivityState())
            }

            override fun onLost(network: Network) {
                trySend(currentConnectivityState())
            }

            override fun onUnavailable() {
                trySend(NetworkConnectivityState.Unavailable)
            }
        }

        trySend(currentConnectivityState())
        val callbackRegistered = runCatching {
            registerNetworkCallback(createNetworkRequest(), callback)
        }.isSuccess

        awaitClose {
            if (callbackRegistered) {
                runCatching {
                    unregisterNetworkCallback(callback)
                }
            }
        }
    }.distinctUntilChanged()
}

private fun ConnectivityManager.currentConnectivityState(): NetworkConnectivityState {
    val activeNetwork = activeNetwork ?: return NetworkConnectivityState.Unavailable
    val capabilities = getNetworkCapabilities(activeNetwork) ?: return NetworkConnectivityState.Unavailable

    return capabilities.toNetworkConnectivityState()
}

private fun createNetworkRequest(): NetworkRequest {
    return NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()
}

private fun NetworkCapabilities.toNetworkConnectivityState(): NetworkConnectivityState {
    val hasInternet = hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    val isValidated = hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

    return NetworkConnectivityState(
        isAvailable = hasInternet,
        isValidated = isValidated,
        transports = toNetworkTransports()
    )
}

private fun NetworkCapabilities.toNetworkTransports(): Set<NetworkTransport> {
    val transports = buildSet {
        if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
            add(NetworkTransport.Wifi)

        if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
            add(NetworkTransport.Cellular)

        if (hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
            add(NetworkTransport.Ethernet)

        if (hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH))
            add(NetworkTransport.Bluetooth)

        if (hasTransport(NetworkCapabilities.TRANSPORT_VPN))
            add(NetworkTransport.Vpn)
    }

    return transports.ifEmpty { setOf(NetworkTransport.Other) }
}
