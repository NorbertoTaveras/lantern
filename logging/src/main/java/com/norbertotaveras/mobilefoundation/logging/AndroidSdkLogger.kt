package com.norbertotaveras.mobilefoundation.logging

import android.util.Log

class AndroidSdkLogger(
    private val tag: String = DEFAULT_TAG,
    private val isEnabled: Boolean = false
) : SdkLogger {

    override fun debug(message: String) {
        if (isEnabled)
            Log.d(tag, message)
    }

    override fun info(message: String) {
        if (isEnabled)
            Log.i(tag, message)
    }

    override fun warning(message: String, throwable: Throwable?) {
        if (isEnabled)
            Log.w(tag, message, throwable)
    }

    override fun error(message: String, throwable: Throwable?) {
        if (isEnabled)
            Log.e(tag, message, throwable)
    }

    private companion object {
        const val DEFAULT_TAG = "MobileFoundationSdk"
    }
}