package com.norbertotaveras.lantern.securestorage

import kotlinx.coroutines.runBlocking

internal fun runBlockingTest(block: suspend () -> Unit) {
    runBlocking {
        block()
    }
}
