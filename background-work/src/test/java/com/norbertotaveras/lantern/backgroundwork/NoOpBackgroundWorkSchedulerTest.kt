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
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NoOpBackgroundWorkSchedulerTest {

    @Test
    fun enqueueStoresWorkInfo() = runBlocking {
        val scheduler = NoOpBackgroundWorkScheduler()
        val name = BackgroundWorkName.unsafe("sync")

        val result = scheduler.enqueue(
            BackgroundWorkRequest(
                name = name,
                type = BackgroundWorkType.OneTime
            )
        )

        assertTrue(result is SdkResult.Success)
        val info = scheduler.getWorkInfo(name)
        assertTrue(info is SdkResult.Success)
        assertEquals(BackgroundWorkStatus.Enqueued, (info as SdkResult.Success).data?.status)
    }

    @Test
    fun cancelMarksWorkCancelled() = runBlocking {
        val scheduler = NoOpBackgroundWorkScheduler()
        val name = BackgroundWorkName.unsafe("sync")

        scheduler.enqueue(
            BackgroundWorkRequest(
                name = name,
                type = BackgroundWorkType.OneTime
            )
        )
        scheduler.cancel(name)

        val info = scheduler.getWorkInfo(name)
        assertTrue(info is SdkResult.Success)
        assertEquals(BackgroundWorkStatus.Cancelled, (info as SdkResult.Success).data?.status)
    }
}
