package com.norbertotaveras.lantern.core

/**
 * Provider-neutral error returned by SDK APIs.
 */
data class SdkError(
    val code: String,
    val message: String,
    val cause: Throwable? = null,
    val metadata: Map<String, String> = emptyMap()
)
