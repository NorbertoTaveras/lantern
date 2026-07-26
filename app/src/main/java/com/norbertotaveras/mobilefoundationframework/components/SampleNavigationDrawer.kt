package com.norbertotaveras.mobilefoundationframework.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.norbertotaveras.mobilefoundationframework.screens.FirebaseAuthScreen
import com.norbertotaveras.mobilefoundationframework.screens.HomeScreen
import com.norbertotaveras.mobilefoundationframework.navigation.SampleDestination
import com.norbertotaveras.mobilefoundationframework.navigation.sampleDestinations
import com.norbertotaveras.mobilefoundationframework.screens.AuthStateScreen
import com.norbertotaveras.mobilefoundationframework.screens.FirebaseGoogleAuthScreen
import com.norbertotaveras.mobilefoundationframework.screens.GoogleAuthScreen
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

                HorizontalDivider()

                sampleDestinations.forEach { destination ->
                    NavigationDrawerItem(
                        label = {
                            Column {
                                Text(text = destination.title)
                                Text(text = destination.description)
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
                        Text(text = currentDestination.title)
                    },
                    navigationIcon = {
                        TextButton(
                            onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }
                        ) {
                            Text(text = "Menu")
                        }
                    }
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
        modifier = Modifier.padding(24.dp)
    ) {
        Text(text = "Mobile Foundation SDK")
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "Sample app")
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