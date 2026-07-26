package com.norbertotaveras.mobilefoundation.core

data class SdkConfig(
    val environment: Enviroment,
    val isDebugLoggingEnabled: Boolean = false
)