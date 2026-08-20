package com.norbertotaveras.mobilefoundation.backgroundwork

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
