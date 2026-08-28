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

package com.norbertotaveras.lantern.backgroundwork

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
