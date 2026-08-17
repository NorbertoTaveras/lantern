package com.norbertotaveras.mobilefoundation.core

data class SdkConfig(
    val environment: Environment,
    val isDebugLoggingEnabled: Boolean = false
)
