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

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.norbertotaveras.lantern.core.SdkResult
import com.norbertotaveras.lantern.mediapicker.MediaMimeType
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickerResult
import com.norbertotaveras.lantern.mediapicker.MediaPickerStatus
import com.norbertotaveras.lantern.mediapicker.MediaSelectionMode
import com.norbertotaveras.lantern.mediapicker.MediaType
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerContractType
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerContracts
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerLauncher
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerMediaPicker
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerRequest
import com.norbertotaveras.lantern.mediapicker.android.AndroidPhotoPickerResultMapper
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import com.norbertotaveras.lanternsample.components.PrimaryDemoButton
import com.norbertotaveras.lanternsample.components.SecondaryDemoButton
import com.norbertotaveras.lanternsample.components.StatusMessage
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

@Composable
fun MediaPickerScreen() {
    val coroutineScope = rememberCoroutineScope()
    val resultMapper = remember { AndroidPhotoPickerResultMapper() }
    var pendingPick by remember { mutableStateOf<PendingMediaPick?>(null) }
    var pickerResult by remember { mutableStateOf(MediaPickerResult.Empty) }
    var message by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isPicking by remember { mutableStateOf(false) }

    val singleLauncher = rememberLauncherForActivityResult(
        contract = AndroidPhotoPickerContracts.single(),
        onResult = { uri ->
            val pending = pendingPick ?: return@rememberLauncherForActivityResult
            pending.deferred.complete(
                resultMapper.mapSingle(
                    uri = uri,
                    request = pending.request.sourceRequest
                )
            )
            pendingPick = null
        }
    )

    val multipleLauncher = rememberLauncherForActivityResult(
        contract = AndroidPhotoPickerContracts.multiple(maxItems = 5),
        onResult = { uris ->
            val pending = pendingPick ?: return@rememberLauncherForActivityResult
            pending.deferred.complete(
                resultMapper.mapMultiple(
                    uris = uris,
                    request = pending.request.sourceRequest
                )
            )
            pendingPick = null
        }
    )

    val mediaPicker = remember(singleLauncher, multipleLauncher) {
        AndroidPhotoPickerMediaPicker(
            launcher = AndroidPhotoPickerLauncher { request ->
                val deferred = CompletableDeferred<MediaPickerResult>()
                pendingPick = PendingMediaPick(
                    request = request,
                    deferred = deferred
                )

                try {
                    when (request.contractType) {
                        AndroidPhotoPickerContractType.Single -> {
                            singleLauncher.launch(request.visualMediaRequest)
                        }
                        AndroidPhotoPickerContractType.Multiple -> {
                            multipleLauncher.launch(request.visualMediaRequest)
                        }
                    }

                    deferred.await()
                } catch (throwable: Throwable) {
                    pendingPick = null
                    throw throwable
                }
            }
        )
    }

    val imageRequest = remember {
        MediaPickRequest(
            mediaTypes = setOf(MediaType.Image),
            selectionMode = MediaSelectionMode.Single
        )
    }
    val mixedRequest = remember {
        MediaPickRequest(
            mediaTypes = setOf(MediaType.Image, MediaType.Video),
            selectionMode = MediaSelectionMode.Multiple,
            maxItems = 5,
            mimeTypes = setOf(MediaMimeType.unsafe("image/jpeg"), MediaMimeType.unsafe("video/mp4"))
        )
    }

    fun pickMedia(request: MediaPickRequest) {
        coroutineScope.launch {
            isPicking = true
            message = null
            errorMessage = null

            try {
                val result = mediaPicker.pick(request)
                pickerResult = result.toPickerResult()
                message = result.toMessage()
                errorMessage = result.toErrorMessage()
            } finally {
                isPicking = false
            }
        }
    }

    FeatureScreen(
        title = "Media Picker",
        subtitle = "Model Android Photo Picker requests and typed results while lifecycle launchers stay in the app.",
        icon = Icons.Filled.Collections,
        status = "Ready"
    ) {
        MetricRow(
            metrics = listOf(
                DemoMetric(label = "Request types", value = "2"),
                DemoMetric(label = "Max sample", value = mixedRequest.maxItems.toString()),
                DemoMetric(label = "Selection", value = pickerResult.status.name)
            )
        )

        StatusMessage(
            message = message,
            errorMessage = errorMessage
        )

        DemoSection(
            title = "Single image request",
            description = "Launches Android Photo Picker through the SDK media picker bridge.",
            leadingIcon = Icons.Filled.Image
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                PrimaryDemoButton(
                    text = "Pick image",
                    icon = Icons.Filled.Image,
                    enabled = !isPicking,
                    onClick = {
                        pickMedia(imageRequest)
                    }
                )
                InfoRow(label = "Media types", value = imageRequest.mediaTypes.joinToString { it.name })
                InfoRow(label = "Selection", value = imageRequest.selectionMode.name)
                InfoRow(label = "Max items", value = imageRequest.maxItems.toString())
            }
        }

        DemoSection(
            title = "Mixed media request",
            description = "Launches multi-select image/video picking with typed SDK request metadata.",
            leadingIcon = Icons.Filled.VideoLibrary
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                SecondaryDemoButton(
                    text = "Pick up to 5 items",
                    icon = Icons.Filled.Collections,
                    enabled = !isPicking,
                    onClick = {
                        pickMedia(mixedRequest)
                    }
                )
                InfoRow(label = "Media types", value = mixedRequest.mediaTypes.joinToString { it.name })
                InfoRow(label = "Selection", value = mixedRequest.selectionMode.name)
                InfoRow(label = "MIME filters", value = mixedRequest.mimeTypes.joinToString { it.value })
                InfoRow(label = "Result model", value = "MediaPickerResult")
            }
        }

        DemoSection(
            title = "Latest picker result",
            description = "Selected URI values are returned by the SDK result model.",
            leadingIcon = Icons.Filled.Collections
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Status", value = pickerResult.status.name)
                InfoRow(label = "Items", value = pickerResult.items.size.toString())
                pickerResult.items.take(3).forEachIndexed { index, item ->
                    InfoRow(
                        label = "Item ${index + 1}",
                        value = item.mediaType.name,
                        supportingText = item.uri
                    )
                }
            }
        }
    }
}

private data class PendingMediaPick(
    val request: AndroidPhotoPickerRequest,
    val deferred: CompletableDeferred<MediaPickerResult>
)

private fun SdkResult<MediaPickerResult>.toPickerResult(): MediaPickerResult {
    return when (this) {
        is SdkResult.Success -> data
        is SdkResult.Failure -> MediaPickerResult.Empty
    }
}

private fun SdkResult<MediaPickerResult>.toMessage(): String? {
    return when (this) {
        is SdkResult.Success -> {
            when {
                data.hasSelection -> "Selected ${data.items.size} item(s)."
                data.status == MediaPickerStatus.Cancelled -> {
                    "Picker cancelled."
                }
                else -> "Picker returned no media."
            }
        }
        is SdkResult.Failure -> null
    }
}

private fun SdkResult<MediaPickerResult>.toErrorMessage(): String? {
    return when (this) {
        is SdkResult.Success -> null
        is SdkResult.Failure -> error.message
    }
}
