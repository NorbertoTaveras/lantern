package com.norbertotaveras.mobilefoundation.backgroundwork

enum class BackgroundWorkStatus {
    Enqueued,
    Running,
    Succeeded,
    Failed,
    Cancelled,
    Blocked
}
