package com.norbertotaveras.mobilefoundation.core

data class SdkError(
    val code: String,
    val message: String,
    val cause: Throwable? = null,
    val metadata: Map<String, String> = emptyMap()
)