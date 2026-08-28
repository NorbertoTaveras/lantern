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

package com.norbertotaveras.lanternsample.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkConstraints
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkName
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkRequest
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkType
import com.norbertotaveras.lantern.backgroundwork.NoOpBackgroundWorkScheduler
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.launch

@Composable
fun BackgroundWorkScreen() {
    val coroutineScope = rememberCoroutineScope()
    val scheduler = remember { NoOpBackgroundWorkScheduler() }
    val workName = remember { BackgroundWorkName.unsafe("sample-sync") }
    val request = remember {
        BackgroundWorkRequest(
            name = workName,
            type = BackgroundWorkType.OneTime,
            constraints = BackgroundWorkConstraints(requiresNetwork = true),
            input = mapOf("source" to "sample-app")
        )
    }
    var currentStatus by remember { mutableStateOf("Not scheduled") }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    FeatureScreen(
        title = "Background Work",
        subtitle = "Schedule, query, and cancel background work through provider-neutral SDK models.",
        icon = Icons.Filled.Work,
        status = currentStatus
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Scheduler", value = "No-op"),
                DemoMetric(label = "Input keys", value = request.input.size.toString()),
                DemoMetric(label = "Constraint", value = "Network")
            )
        )

        DemoSection(
            title = "Work controls",
            description = "NoOpBackgroundWorkScheduler gives the sample app a safe in-memory demo path.",
            leadingIcon = Icons.Filled.Schedule
        ) {
            PrimaryDemoButton(
                text = "Enqueue sample work",
                icon = Icons.Filled.Schedule,
                onClick = {
                    coroutineScope.launch {
                        when (val result = scheduler.enqueue(request)) {
                            is SdkResult.Success -> {
                                currentStatus = "Enqueued"
                                errorMessage = null
                                message = "Work ${result.data.value.take(8)} enqueued."
                            }
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Cancel sample work",
                icon = Icons.Filled.Cancel,
                onClick = {
                    coroutineScope.launch {
                        when (val result = scheduler.cancel(workName)) {
                            is SdkResult.Success -> {
                                currentStatus = "Cancelled"
                                errorMessage = null
                                message = "Work '${workName.value}' cancelled."
                            }
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )
        }

        DemoSection(
            title = "Request model",
            description = "The WorkManager implementation uses the same request shape with app-owned Worker classes.",
            leadingIcon = Icons.Filled.Work
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Name", value = request.name.value)
                InfoRow(label = "Type", value = "OneTime")
                InfoRow(label = "Requires network", value = request.constraints.requiresNetwork.toString())
                InfoRow(label = "App owns", value = "Worker classes")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}
