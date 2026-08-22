package com.norbertotaveras.lantern.logging

import android.util.Log

/**
 * [SdkLogger] implementation that delegates to Android's platform logger.
 */
class AndroidSdkLogger(
    private val tag: String = DEFAULT_TAG,
    private val isEnabled: Boolean = false
) : SdkLogger {

    /**
     * Logs a debug message when logging is enabled.
     */
    override fun debug(message: String) {
        if (isEnabled)
            Log.d(tag, message)
    }

    /**
     * Logs an informational message when logging is enabled.
     */
    override fun info(message: String) {
        if (isEnabled)
            Log.i(tag, message)
    }

    /**
     * Logs a warning message and optional [throwable] when logging is enabled.
     */
    override fun warning(message: String, throwable: Throwable?) {
        if (isEnabled)
            Log.w(tag, message, throwable)
    }

    /**
     * Logs an error message and optional [throwable] when logging is enabled.
     */
    override fun error(message: String, throwable: Throwable?) {
        if (isEnabled)
            Log.e(tag, message, throwable)
    }

    private companion object {
        const val DEFAULT_TAG = "LanternSdk"
    }
}
