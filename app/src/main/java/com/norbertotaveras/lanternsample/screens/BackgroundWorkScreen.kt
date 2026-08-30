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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Work
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkConstraints
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkName
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkPolicy
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkRequest
import com.norbertotaveras.lantern.backgroundwork.BackgroundWorkType
import com.norbertotaveras.lantern.backgroundwork.WorkManagerBackgroundWorkScheduler
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lanternsample.background.SampleSyncWorker
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val workName = remember { BackgroundWorkName.unsafe("sample-sync") }
    val scheduler = remember(context, workName) {
        WorkManagerBackgroundWorkScheduler(
            context = context,
            workerClasses = mapOf(workName to SampleSyncWorker::class.java)
        )
    }
    val request = remember {
        BackgroundWorkRequest(
            name = workName,
            type = BackgroundWorkType.OneTime,
            policy = BackgroundWorkPolicy.ReplaceExisting,
            constraints = BackgroundWorkConstraints.None,
            input = mapOf("source" to "sample-app")
        )
    }
    val observedWorkInfo by scheduler.observeWorkInfo(workName).collectAsState(initial = null)
    var currentStatus by remember { mutableStateOf("Not scheduled") }
    var currentWorkId by remember { mutableStateOf<String?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val latestStatus = observedWorkInfo?.status?.name ?: currentStatus
    val latestWorkId = observedWorkInfo?.id?.value ?: currentWorkId
    val progressText = observedWorkInfo?.progress?.entries
        ?.joinToString { (key, value) -> "$key=$value" }
        .orEmpty()
    val outputText = observedWorkInfo?.output?.entries
        ?.joinToString { (key, value) -> "$key=$value" }
        .orEmpty()

    FeatureScreen(
        title = "Background Work",
        subtitle = "Schedule, query, and cancel background work through provider-neutral SDK models.",
        icon = Icons.Filled.Work,
        status = latestStatus
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Scheduler", value = "WorkManager"),
                DemoMetric(label = "Input keys", value = request.input.size.toString()),
                DemoMetric(label = "Constraint", value = "None")
            )
        )

        DemoSection(
            title = "Work controls",
            description = "The sample app registers its own Worker while Lantern owns the provider-neutral scheduler API.",
            leadingIcon = Icons.Filled.Schedule
        ) {
            PrimaryDemoButton(
                text = "Run sample sync",
                icon = Icons.Filled.Schedule,
                onClick = {
                    coroutineScope.launch {
                        when (val result = scheduler.enqueue(request)) {
                            is SdkResult.Success -> {
                                currentStatus = "Enqueued"
                                currentWorkId = result.data.value
                                errorMessage = null
                                message = "Work ${result.data.value.take(8)} submitted to WorkManager."
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
                                message = "Work '${workName.value}' cancelled in WorkManager."
                            }
                            is SdkResult.Failure -> errorMessage = result.error.message
                        }
                    }
                }
            )

            SecondaryDemoButton(
                text = "Read work status",
                icon = Icons.Filled.Refresh,
                onClick = {
                    coroutineScope.launch {
                        when (val result = scheduler.getWorkInfo(workName)) {
                            is SdkResult.Success -> {
                                currentStatus = result.data?.status?.name ?: "Not scheduled"
                                currentWorkId = result.data?.id?.value
                                errorMessage = null
                                message = result.data?.let {
                                    "Work '${it.name.value}' is ${it.status.name}."
                                } ?: "No work has been scheduled."
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
                InfoRow(label = "Latest ID", value = latestWorkId?.take(8) ?: "None")
                InfoRow(label = "Latest status", value = latestStatus)
                InfoRow(label = "Type", value = "OneTime")
                InfoRow(label = "Policy", value = request.policy.name)
                InfoRow(label = "Progress", value = progressText.ifBlank { "None" })
                InfoRow(label = "Output", value = outputText.ifBlank { "None" })
                InfoRow(label = "App owns", value = "Worker classes")
            }
        }

        StatusMessage(message = message, errorMessage = errorMessage)
    }
}
