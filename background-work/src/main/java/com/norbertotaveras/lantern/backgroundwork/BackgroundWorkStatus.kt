package com.norbertotaveras.lantern.backgroundwork

/**
 * Provider-neutral status for scheduled background work.
 */
enum class BackgroundWorkStatus {
    Enqueued,
    Running,
    Succeeded,
    Failed,
    Cancelled,
    Blocked
}
