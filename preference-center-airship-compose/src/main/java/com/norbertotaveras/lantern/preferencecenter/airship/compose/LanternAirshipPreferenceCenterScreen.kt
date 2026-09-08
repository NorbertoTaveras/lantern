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

package com.norbertotaveras.lantern.preferencecenter.airship.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.urbanairship.preferencecenter.compose.ui.PreferenceCenterScreen
import com.urbanairship.preferencecenter.compose.ui.theme.PreferenceCenterTheme

/**
 * Displays an Airship Preference Center with Airship's official Compose UI.
 *
 * Airship initialization, Preference Center identifiers, legal copy, subscription taxonomy,
 * styling decisions, and navigation ownership stay with the consuming app.
 */
@Composable
public fun LanternAirshipPreferenceCenterScreen(
    identifier: String,
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit = {},
) {
    PreferenceCenterTheme {
        PreferenceCenterScreen(
            identifier = identifier,
            modifier = modifier,
            onNavigateUp = onNavigateUp,
        )
    }
}
