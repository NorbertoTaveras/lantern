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
 * Provider-neutral request to enqueue background work.
 */
data class BackgroundWorkRequest(
    val name: BackgroundWorkName,
    val type: BackgroundWorkType,
    val policy: BackgroundWorkPolicy = BackgroundWorkPolicy.KeepExisting,
    val constraints: BackgroundWorkConstraints = BackgroundWorkConstraints.None,
    val input: Map<String, String> = emptyMap(),
    val initialDelayMillis: Long = 0L
)
