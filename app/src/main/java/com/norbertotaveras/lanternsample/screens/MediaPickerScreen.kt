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
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoLibrary
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.norbertotaveras.lantern.mediapicker.MediaMimeType
import com.norbertotaveras.lantern.mediapicker.MediaPickRequest
import com.norbertotaveras.lantern.mediapicker.MediaPickerResult
import com.norbertotaveras.lantern.mediapicker.MediaSelectionMode
import com.norbertotaveras.lantern.mediapicker.MediaType
import com.norbertotaveras.lanternsample.components.DemoMetric
import com.norbertotaveras.lanternsample.components.DemoSection
import com.norbertotaveras.lanternsample.components.FeatureScreen
import com.norbertotaveras.lanternsample.components.InfoRow
import com.norbertotaveras.lanternsample.components.MetricRow
import androidx.compose.ui.unit.dp

@Composable
fun MediaPickerScreen() {
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
    val emptyResult = remember { MediaPickerResult.Empty }

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
                DemoMetric(label = "Selection", value = emptyResult.status.name)
            )
        )

        DemoSection(
            title = "Single image request",
            description = "Provider-neutral request modeling that can be passed to AndroidPhotoPickerMediaPicker.",
            leadingIcon = Icons.Filled.Image
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Media types", value = imageRequest.mediaTypes.joinToString { it.name })
                InfoRow(label = "Selection", value = imageRequest.selectionMode.name)
                InfoRow(label = "Max items", value = imageRequest.maxItems.toString())
            }
        }

        DemoSection(
            title = "Mixed media request",
            description = "Multiple selection combines images, videos, optional MIME filters, and item limits.",
            leadingIcon = Icons.Filled.VideoLibrary
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoRow(label = "Media types", value = mixedRequest.mediaTypes.joinToString { it.name })
                InfoRow(label = "Selection", value = mixedRequest.selectionMode.name)
                InfoRow(label = "MIME filters", value = mixedRequest.mimeTypes.joinToString { it.value })
                InfoRow(label = "Result model", value = "MediaPickerResult")
            }
        }
    }
}
