package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thelazybattley.joserizalquizadmin.presentation.feature.home.HomeTabScreen
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    AppTheme {
        Scaffold(
            modifier = Modifier,
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    modifier = Modifier
                )
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .background(color = APP_BACKGROUND)
                    .padding(paddingValues = innerPadding)
                    .padding(all = APP_PADDING)
                    .fillMaxSize(),
                navController = navController,
                startDestination = AppDestinations.BottomNavDestinations.Home.route
            ) {
                composable(route = AppDestinations.BottomNavDestinations.Home.route) {
                    HomeTabScreen(modifier = Modifier.fillMaxSize())
                }
                composable(route = AppDestinations.BottomNavDestinations.Moderate.route) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Moderate")
                    }
                }
                composable(route = AppDestinations.BottomNavDestinations.Content.route) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Content")
                    }
                }
                composable(route = AppDestinations.BottomNavDestinations.More.route) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "More")
                    }
                }
            }
        }
    }
}
