package com.norbertotaveras.lantern.backgroundwork

/**
 * Policy applied when unique background work already exists.
 */
enum class BackgroundWorkPolicy {
    KeepExisting,
    ReplaceExisting,
    Append
}
