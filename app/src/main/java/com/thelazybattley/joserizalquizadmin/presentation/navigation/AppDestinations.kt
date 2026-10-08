package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

sealed class AppDestinations(val route: String, val routeWithArgs: String? = null) {

    sealed class BottomNavDestinations(
        val bottomNavRoute: String, @DrawableRes val drawable: Int,
        @StringRes val textRes: Int
    ) : AppDestinations(route = "") {
        object Moderate : BottomNavDestinations(
            bottomNavRoute = "moderate",
            drawable = R.drawable.ic_flag,
            textRes = R.string.moderate
        )

        object Content : BottomNavDestinations(
            bottomNavRoute = "content",
            drawable = R.drawable.ic_add,
            textRes = R.string.content
        )

        object Release : BottomNavDestinations(
            bottomNavRoute = "release",
            drawable = R.drawable.ic_arrow,
            textRes = R.string.release
        )

        companion object {
            fun routes() = listOf(
                Moderate, Content, Release
            )
        }
    }

    object Login : AppDestinations(route = "login")

    object AddBook : AppDestinations(route = "add_book")

    object AddQuestion :
        AppDestinations(
            route = "add_question",
            routeWithArgs = "add_question/{$QUIZ_ID}/{$CHAPTER_NUMBER}"
        ) {
        fun createRoute(quizId: String, chapterNumber: Int) = "add_question/$quizId/$chapterNumber"
    }

    companion object {
        const val QUIZ_ID = "quizId"
        const val CHAPTER_NUMBER = "chapterNumber"
    }
}
