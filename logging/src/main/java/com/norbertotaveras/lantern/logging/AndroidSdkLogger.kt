/*
 * Copyright (C) 2026 Norberto Taveras
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
