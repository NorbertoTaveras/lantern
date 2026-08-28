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

package com.norbertotaveras.lantern.securestorage

/**
 * Stable error codes returned by secure storage APIs.
 */
object SecureStorageErrorCodes {
    /**
     * The provided [SecureStorageKey] value failed validation.
     */
    const val INVALID_KEY = "secure_storage_invalid_key"
    /**
     * A read operation failed.
     */
    const val READ_FAILED = "secure_storage_read_failed"
    /**
     * A write operation failed.
     */
    const val WRITE_FAILED = "secure_storage_write_failed"
    /**
     * A remove operation failed.
     */
    const val REMOVE_FAILED = "secure_storage_remove_failed"
    /**
     * A clear operation failed.
     */
    const val CLEAR_FAILED = "secure_storage_clear_failed"
    /**
     * Fallback code for unexpected secure storage failures.
     */
    const val UNKNOWN = "secure_storage_unknown"
}
