package com.norbertotaveras.mobilefoundation.securestorage

import kotlinx.coroutines.runBlocking

internal fun runBlockingTest(block: suspend () -> Unit) {
    runBlocking {
        block()
    }
}
