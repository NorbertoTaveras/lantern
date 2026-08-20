package com.norbertotaveras.mobilefoundation.core

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides coroutine dispatchers so SDK implementations can be tested without hardcoded dispatchers.
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
