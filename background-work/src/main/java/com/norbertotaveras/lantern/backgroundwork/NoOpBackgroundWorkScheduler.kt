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

import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.backgroundwork.internal.BackgroundWorkRequestValidator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.util.UUID

/**
 * In-memory scheduler useful for tests, demos, and hosts without WorkManager.
 */
class NoOpBackgroundWorkScheduler : BackgroundWorkScheduler {
    private val workInfo = MutableStateFlow<Map<BackgroundWorkName, BackgroundWorkInfo>>(emptyMap())

    override suspend fun enqueue(request: BackgroundWorkRequest): SdkResult<BackgroundWorkId> {
        val validationError = BackgroundWorkRequestValidator.validate(request)
        if (validationError != null) {
            return SdkResult.Failure(validationError)
        }

        val id = BackgroundWorkId.unsafe(UUID.randomUUID().toString())
        workInfo.value = workInfo.value + (
            request.name to BackgroundWorkInfo(
                id = id,
                name = request.name,
                status = BackgroundWorkStatus.Enqueued
            )
        )
        return SdkResult.Success(id)
    }

    override suspend fun cancel(name: BackgroundWorkName): SdkResult<Unit> {
        val info = workInfo.value[name] ?: return SdkResult.Success(Unit)
        workInfo.value = workInfo.value + (name to info.copy(status = BackgroundWorkStatus.Cancelled))
        return SdkResult.Success(Unit)
    }

    override suspend fun getWorkInfo(name: BackgroundWorkName): SdkResult<BackgroundWorkInfo?> {
        return SdkResult.Success(workInfo.value[name])
    }

    override fun observeWorkInfo(name: BackgroundWorkName): Flow<BackgroundWorkInfo?> {
        return workInfo.map { it[name] }
    }
}
