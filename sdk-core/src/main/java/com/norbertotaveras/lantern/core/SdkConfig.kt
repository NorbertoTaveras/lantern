package com.norbertotaveras.lantern.core

/**
 * Shared SDK configuration used by modules that need environment-level behavior.
 */
data class SdkConfig(
    val environment: Environment,
    val isDebugLoggingEnabled: Boolean = false
)
