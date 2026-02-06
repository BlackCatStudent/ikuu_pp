package com.ikuuvpn.app.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ikuuvpn.app.presentation.auth.LoginScreen
import com.ikuuvpn.app.presentation.settings.SettingsScreen
import com.ikuuvpn.app.presentation.subscription.ImportSubscriptionDialog
import com.ikuuvpn.app.presentation.subscription.SubscriptionScreen
import com.ikuuvpn.app.presentation.subscription.viewmodel.SubscriptionViewModel
import androidx.hilt.navigation.compose.hiltViewModel

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
    object Subscriptions : Screen("subscriptions")
    object Nodes : Screen("nodes")
    object Settings : Screen("settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: SubscriptionViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showImportDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            if (currentDestination?.route != Screen.Login.route) {
                NavigationBar {
                    listOf(
                        Screen.Home,
                        Screen.Subscriptions,
                        Screen.Nodes,
                        Screen.Settings
                    ).forEach { screen ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    when (screen) {
                                        Screen.Home -> Icons.Default.Home
                                        Screen.Subscriptions -> Icons.Default.CloudDownload
                                        Screen.Nodes -> Icons.Default.Router
                                        Screen.Settings -> Icons.Default.Settings
                                    },
                                    contentDescription = null
                                )
                            },
                            label = { Text(screen.route) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(Screen.Home.route) {
                HomeScreen()
            }

            composable(Screen.Subscriptions.route) {
                SubscriptionScreen(
                    onImportClick = { showImportDialog = true }
                )
            }

            composable(Screen.Nodes.route) {
                NodesScreen()
            }

            composable(Screen.Settings.route) {
                SettingsScreen()
            }
        }
    }

    if (showImportDialog) {
        ImportSubscriptionDialog(
            onDismiss = { showImportDialog = false },
            onImport = { url, name ->
                viewModel.importSubscription(url, name)
                showImportDialog = false
            }
        )
    }
}

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "首页",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Composable
fun NodesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "节点",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}