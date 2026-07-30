package com.norbertotaveras.mobilefoundation.network.okhttp

interface TokenProvider {
    fun getAccessToken(): String?
}
