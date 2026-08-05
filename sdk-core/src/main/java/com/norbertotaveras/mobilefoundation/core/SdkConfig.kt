package com.norbertotaveras.mobilefoundation.core

data class SdkConfig(
    val environment: Environment,
    val isDebugLoggingEnabled: Boolean = false
) {
    @Deprecated(
        message = "Use the constructor that accepts Environment instead.",
        replaceWith = ReplaceWith("SdkConfig(environment.toEnvironment(), isDebugLoggingEnabled)")
    )
    @Suppress("DEPRECATION")
    constructor(
        environment: Enviroment,
        isDebugLoggingEnabled: Boolean = false
    ) : this(
        environment = environment.toEnvironment(),
        isDebugLoggingEnabled = isDebugLoggingEnabled
    )
}
