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

package com.norbertotaveras.lanternsample.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.norbertotaveras.lanternsample.BuildConfig
import com.norbertotaveras.lanternsample.R
import com.norbertotaveras.lanternsample.navigation.SampleDestination
import com.norbertotaveras.lanternsample.navigation.sampleDestinations
import com.norbertotaveras.lanternsample.screens.AuthStateScreen
import com.norbertotaveras.lanternsample.screens.AnalyticsScreen
import com.norbertotaveras.lanternsample.screens.AppVersioningScreen
import com.norbertotaveras.lanternsample.screens.BackgroundWorkScreen
import com.norbertotaveras.lanternsample.screens.DeepLinksScreen
import com.norbertotaveras.lanternsample.screens.FeatureFlagsScreen
import com.norbertotaveras.lanternsample.screens.FirebaseAuthScreen
import com.norbertotaveras.lanternsample.screens.FirebaseGoogleAuthScreen
import com.norbertotaveras.lanternsample.screens.GoogleAuthScreen
import com.norbertotaveras.lanternsample.screens.HomeScreen
import com.norbertotaveras.lanternsample.screens.LoggingScreen
import com.norbertotaveras.lanternsample.screens.MediaPickerScreen
import com.norbertotaveras.lanternsample.screens.NetworkScreen
import com.norbertotaveras.lanternsample.screens.NotificationsScreen
import com.norbertotaveras.lanternsample.screens.PermissionsScreen
import com.norbertotaveras.lanternsample.screens.RemoteConfigScreen
import com.norbertotaveras.lanternsample.screens.SecureStorageScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SampleNavigationDrawer() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: SampleDestination.Home.route

    val currentDestination = sampleDestinations.firstOrNull { it.route == currentRoute }
        ?: SampleDestination.Home

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier.widthIn(max = 360.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        DrawerHeader(version = BuildConfig.LANTERN_VERSION)

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                        )

                        Text(
                            text = "SDK samples",
                            modifier = Modifier.padding(horizontal = 28.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        sampleDestinations.forEach { destination ->
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Column {
                                        Text(
                                            text = destination.title,
                                            fontWeight = FontWeight.Medium
                                        )
                                        Text(
                                            text = destination.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                badge = {
                                    destination.drawerBadge()?.let {
                                        StatusPill(text = it)
                                    }
                                },
                                selected = currentRoute == destination.route,
                                onClick = {
                                    coroutineScope.launch {
                                        drawerState.close()
                                    }

                                    navController.navigate(destination.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }

                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                        }
                    }

                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                    )

                    Text(
                        text = "Sample app owns UI, Firebase config, and manual verification.",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(text = currentDestination.title)
                            Text(
                                text = currentDestination.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Open navigation drawer"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        ) { innerPadding ->
            SampleNavHost(
                contentPadding = innerPadding,
                startDestination = SampleDestination.Home.route,
                navController = navController
            )
        }
    }
}

@Composable
private fun DrawerHeader(version: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.clip(CircleShape),
                color = MaterialTheme.colorScheme.surfaceContainerHighest
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lantern_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(44.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            StatusPill(text = "Demo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lantern",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = "Production-style Android library demo",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row {
            StatusPill(text = "v$version")
        }
    }
}

private fun SampleDestination.drawerBadge(): String? {
    return when (this) {
        SampleDestination.FirebaseAuth,
        SampleDestination.GoogleAuth,
        SampleDestination.FirebaseGoogleAuth,
        SampleDestination.Permissions,
        SampleDestination.SecureStorage,
        SampleDestination.RemoteConfig,
        SampleDestination.FeatureFlags,
        SampleDestination.Network,
        SampleDestination.Notifications,
        SampleDestination.MediaPicker,
        SampleDestination.Analytics,
        SampleDestination.DeepLinks,
        SampleDestination.BackgroundWork,
        SampleDestination.AppVersioning -> "Live"

        SampleDestination.Home -> "Now"
        SampleDestination.AuthState,
        SampleDestination.Logging -> null
    }
}

@Composable
private fun SampleNavHost(
    contentPadding: PaddingValues,
    startDestination: String,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)
    ) {
        composable(SampleDestination.Home.route) {
            HomeScreen()
        }

        composable(SampleDestination.FirebaseAuth.route) {
            FirebaseAuthScreen()
        }

        composable(SampleDestination.GoogleAuth.route) {
            GoogleAuthScreen()
        }

        composable(SampleDestination.FirebaseGoogleAuth.route) {
            FirebaseGoogleAuthScreen()
        }

        composable(SampleDestination.AuthState.route) {
            AuthStateScreen()
        }

        composable(SampleDestination.Permissions.route) {
            PermissionsScreen()
        }

        composable(SampleDestination.SecureStorage.route) {
            SecureStorageScreen()
        }

        composable(SampleDestination.RemoteConfig.route) {
            RemoteConfigScreen()
        }

        composable(SampleDestination.FeatureFlags.route) {
            FeatureFlagsScreen()
        }

        composable(SampleDestination.Network.route) {
            NetworkScreen()
        }

        composable(SampleDestination.Notifications.route) {
            NotificationsScreen()
        }

        composable(SampleDestination.MediaPicker.route) {
            MediaPickerScreen()
        }

        composable(SampleDestination.Analytics.route) {
            AnalyticsScreen()
        }

        composable(SampleDestination.DeepLinks.route) {
            DeepLinksScreen()
        }

        composable(SampleDestination.BackgroundWork.route) {
            BackgroundWorkScreen()
        }

        composable(SampleDestination.AppVersioning.route) {
            AppVersioningScreen()
        }

        composable(SampleDestination.Logging.route) {
            LoggingScreen()
        }
    }
}
