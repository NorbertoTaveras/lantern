package com.norbertotaveras.mobilefoundation.backgroundwork

/**
 * Stable error codes returned by background-work APIs.
 */
object BackgroundWorkErrorCodes {
    const val INVALID_WORK_ID = "background_work_invalid_work_id"
    const val INVALID_WORK_NAME = "background_work_invalid_work_name"
    const val INVALID_INTERVAL = "background_work_invalid_interval"
    const val INVALID_INITIAL_DELAY = "background_work_invalid_initial_delay"
    const val ENQUEUE_FAILED = "background_work_enqueue_failed"
    const val CANCEL_FAILED = "background_work_cancel_failed"
    const val QUERY_FAILED = "background_work_query_failed"
    const val WORKER_NOT_REGISTERED = "background_work_worker_not_registered"
}
