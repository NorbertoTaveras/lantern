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

package com.norbertotaveras.lanternsample.background

import android.content.Context
import androidx.work.Data
import androidx.work.Worker
import androidx.work.WorkerParameters

class SampleSyncWorker(
    context: Context,
    workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val source = inputData.getString("source") ?: "sample-app"
        val progress = Data.Builder()
            .putString("step", "syncing")
            .build()

        setProgressAsync(progress).get()
        Thread.sleep(SAMPLE_WORK_DURATION_MILLIS)

        val output = Data.Builder()
            .putString("result", "completed")
            .putString("source", source)
            .build()

        return Result.success(output)
    }

    private companion object {
        const val SAMPLE_WORK_DURATION_MILLIS = 750L
    }
}
