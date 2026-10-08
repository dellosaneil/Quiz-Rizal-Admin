package com.thelazybattley.joserizalquizadmin.presentation.navigation

import android.net.Uri
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

    // Title and author are optional, pre-filled when adding a book from a student suggestion.
    object AddBook : AppDestinations(
        route = "add_book",
        routeWithArgs = "add_book?$BOOK_TITLE={$BOOK_TITLE}&$AUTHOR={$AUTHOR}"
    ) {
        fun createRoute(bookTitle: String? = null, author: String? = null): String {
            if (bookTitle == null && author == null) return route
            return "add_book?$BOOK_TITLE=${Uri.encode(bookTitle.orEmpty())}&$AUTHOR=${Uri.encode(author.orEmpty())}"
        }
    }

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
        const val BOOK_TITLE = "bookTitle"
        const val AUTHOR = "author"
    }
}
