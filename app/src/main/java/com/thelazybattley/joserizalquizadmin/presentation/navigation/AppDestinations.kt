package com.thelazybattley.joserizalquizadmin.presentation.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R

sealed class AppDestinations(val route: String, val routeWithArgs: String? = null) {

    sealed class BottomNavDestinations(
        val bottomNavRoute: String, @DrawableRes val drawable: Int,
        @StringRes val textRes: Int
    ) : AppDestinations(route = "") {
        object Home : BottomNavDestinations(
            bottomNavRoute = "home",
            drawable = R.drawable.ic_home,
            textRes = R.string.home
        )

        object Moderate : BottomNavDestinations(
            bottomNavRoute = "moderate",
            drawable = R.drawable.ic_flag,
            textRes = R.string.moderate
        )

        object Content : BottomNavDestinations(
            bottomNavRoute = "content",
            drawable = R.drawable.ic_content,
            textRes = R.string.content
        )

        object More : BottomNavDestinations(
            bottomNavRoute = "more",
            drawable = R.drawable.ic_more,
            textRes = R.string.more
        )

        companion object {
            fun routes() = listOf(
                Home, Moderate, Content, More
            )
        }
    }

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
