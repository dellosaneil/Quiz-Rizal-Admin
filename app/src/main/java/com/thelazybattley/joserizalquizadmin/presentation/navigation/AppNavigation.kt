package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui.AddBookScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui.AddQuestionScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui.ContentTabScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.LoginDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.login.ui.LoginScreen
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateViewModel
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateScreen
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.CHAPTER_NUMBER
import com.thelazybattley.joserizalquizadmin.presentation.navigation.AppDestinations.Companion.QUIZ_ID
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_PADDING

@Composable
fun AppNavigation(isSignedIn: Boolean) {
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
                startDestination = if (isSignedIn) {
                    AppDestinations.BottomNavDestinations.Moderate.bottomNavRoute
                } else {
                    AppDestinations.Login.route
                }
            ) {
                composable(route = AppDestinations.Login.route) {
                    LoginScreen(modifier = Modifier.fillMaxSize()) { destination ->
                        when (destination) {
                            LoginDestinations.HOME -> navController.navigate(
                                route = AppDestinations.BottomNavDestinations.Moderate.bottomNavRoute
                            ) {
                                popUpTo(route = AppDestinations.Login.route) { inclusive = true }
                            }
                        }
                    }
                }
                composable(route = AppDestinations.BottomNavDestinations.Moderate.bottomNavRoute) {
                    val viewModel = hiltViewModel<ModerateViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()
                    ModerateScreen(
                        state = state,
                        callbacks = viewModel,
                        modifier = Modifier.fillMaxSize()
                    ) { destination ->
                        when (destination) {
                            is ModerateDestinations.AddBook -> navController.navigate(
                                route = AppDestinations.AddBook.createRoute(
                                    bookTitle = destination.bookTitle,
                                    author = destination.author
                                )
                            )
                        }
                    }
                }
                composable(route = AppDestinations.BottomNavDestinations.Content.bottomNavRoute) {
                    ContentTabScreen(
                        modifier = Modifier.fillMaxSize(),
                        navigate = { destination ->
                            when (destination) {
                                ContentDestinations.AddBook -> navController.navigate(
                                    AppDestinations.AddBook.createRoute()
                                )

                                is ContentDestinations.AddQuestion -> navController.navigate(
                                    route = AppDestinations.AddQuestion.createRoute(
                                        quizId = destination.quizId,
                                        chapterNumber = destination.chapterNumber
                                    )
                                )
                            }
                        }
                    )
                }
                composable(route = AppDestinations.BottomNavDestinations.Release.bottomNavRoute) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Text(text = "Release")
                    }
                }
                composable(
                    route = AppDestinations.AddBook.routeWithArgs!!,
                    arguments = listOf(
                        navArgument(name = AppDestinations.BOOK_TITLE) {
                            type = NavType.StringType
                            defaultValue = ""
                        },
                        navArgument(name = AppDestinations.AUTHOR) {
                            type = NavType.StringType
                            defaultValue = ""
                        }
                    )
                ) {
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
                    route = AppDestinations.AddQuestion.routeWithArgs!!,
                    arguments = listOf(
                        navArgument(name = QUIZ_ID) {
                            type = NavType.StringType
                        },
                        navArgument(name = CHAPTER_NUMBER) {
                            type = NavType.IntType
                        }
                    )
                ) {
                    AddQuestionScreen(modifier = Modifier.fillMaxSize()) { destination ->
                        when (destination) {
                            AddQuestionDestinations.Back -> navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
