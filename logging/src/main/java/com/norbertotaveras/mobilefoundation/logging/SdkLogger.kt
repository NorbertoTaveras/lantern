package com.norbertotaveras.mobilefoundation.logging

/**
 * Logging abstraction used by SDK modules without forcing a logging backend on consumers.
 */
interface SdkLogger {
    /**
     * Emits a debug message.
     */
    fun debug(message: String)
    /**
     * Emits an informational message.
     */
    fun info(message: String)
    /**
     * Emits a warning message with optional failure context.
     */
    fun warning(message: String, throwable: Throwable? = null)
    /**
     * Emits an error message with optional failure context.
     */
    fun error(message: String, throwable: Throwable? = null)
}
