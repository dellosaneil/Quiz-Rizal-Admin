package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui.AddBookScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui.AddQuestionScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui.ContentTabScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.home.HomeTabScreen
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val showBottomBar = remember { mutableStateOf(value = true) }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        showBottomBar.value = destination.route in AppDestinations.BottomNavDestinations.routes()
            .map { it.bottomNavRoute }
    }
    AppTheme {
        Scaffold(
            modifier = Modifier,
            bottomBar = {
                if (showBottomBar.value) {
                    BottomNavBar(
                        navController = navController,
                        modifier = Modifier
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .background(color = APP_BACKGROUND)
                    .padding(paddingValues = innerPadding)
                    .padding(all = APP_PADDING)
                    .fillMaxSize(),
                navController = navController,
                startDestination = AppDestinations.AddQuestion.route
            ) {
                composable(route = AppDestinations.BottomNavDestinations.Home.bottomNavRoute) {
                    HomeTabScreen(modifier = Modifier.fillMaxSize())
                }
                composable(route = AppDestinations.BottomNavDestinations.Moderate.bottomNavRoute) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Moderate")
                    }
                }
                composable(route = AppDestinations.BottomNavDestinations.Content.bottomNavRoute) {
                    ContentTabScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigate = { destination ->
                            when (destination) {
                                ContentDestinations.AddBook -> navController.navigate(
                                    AppDestinations.AddBook.route
                                )
                            }
                        }
                    )
                }
                composable(route = AppDestinations.BottomNavDestinations.More.bottomNavRoute) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "More")
                    }
                }
                composable(route = AppDestinations.AddBook.route) {
                    AddBookScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigate = { destination ->
                            when (destination) {
                                AddBookDestinations.BACK -> {
                                    navController.popBackStack()
                                }
                            }
                        }
                    )
                }
                composable(
                    route = AppDestinations.AddQuestion.route!!,
                    arguments = listOf(
//                        navArgument(name = QUIZ_ID) {
//                            type = NavType.StringType
//                        },
//                        navArgument(name = CHAPTER_NUMBER) {
//                            type = NavType.IntType
//                        }
                    )
                ) {
                    AddQuestionScreen()
                }
            }
        }
    }
}
