package com.norbertotaveras.mobilefoundation.network.okhttp

data class NetworkConnectivityState(
    val isAvailable: Boolean,
    val isValidated: Boolean = false,
    val transports: Set<NetworkTransport> = emptySet()
) {
    val isUsable: Boolean
        get() = isAvailable && isValidated

    companion object {
        val Unavailable = NetworkConnectivityState(isAvailable = false)
    }
}
