package com.norbertotaveras.mobilefoundationframework.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AssistChip
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.norbertotaveras.mobilefoundationframework.navigation.SampleDestination
import com.norbertotaveras.mobilefoundationframework.navigation.sampleDestinations
import com.norbertotaveras.mobilefoundationframework.screens.AuthStateScreen
import com.norbertotaveras.mobilefoundationframework.screens.FirebaseAuthScreen
import com.norbertotaveras.mobilefoundationframework.screens.FirebaseGoogleAuthScreen
import com.norbertotaveras.mobilefoundationframework.screens.GoogleAuthScreen
import com.norbertotaveras.mobilefoundationframework.screens.HomeScreen
import com.norbertotaveras.mobilefoundationframework.screens.LoggingScreen
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
            ModalDrawerSheet {
                DrawerHeader()

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
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
                            if (
                                destination == SampleDestination.FirebaseAuth ||
                                destination == SampleDestination.GoogleAuth ||
                                destination == SampleDestination.FirebaseGoogleAuth
                            ) {
                                Text(text = "Live")
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
private fun DrawerHeader() {
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
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Icon(
                    imageVector = SampleDestination.FirebaseGoogleAuth.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(14.dp)
                        .size(28.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AssistChip(
                onClick = {},
                label = {
                    Text(text = "Demo")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Mobile Foundation SDK",
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
            AssistChip(
                onClick = {},
                label = {
                    Text(text = "v0.1.0-dev")
                }
            )
        }
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

        composable(SampleDestination.Logging.route) {
            LoggingScreen()
        }
    }
}
