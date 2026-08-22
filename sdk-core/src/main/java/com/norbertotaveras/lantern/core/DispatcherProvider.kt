package com.norbertotaveras.lantern.core

import kotlinx.coroutines.CoroutineDispatcher

/**
 * Provides coroutine dispatchers so SDK implementations can be tested without hardcoded dispatchers.
 */
interface DispatcherProvider {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}
