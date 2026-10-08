package com.thelazybattley.joserizalquizadmin.presentation.navigation

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

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

    // Title and author are pre-filled from a student suggestion; category is pre-selected from a Content filter.
    object AddBook : AppDestinations(
        route = "add_book",
        routeWithArgs = "add_book?$BOOK_TITLE={$BOOK_TITLE}&$AUTHOR={$AUTHOR}&$CATEGORY={$CATEGORY}"
    ) {
        fun createRoute(bookTitle: String? = null, author: String? = null, category: Category? = null): String {
            if (bookTitle == null && author == null && category == null) return route
            return "add_book?$BOOK_TITLE=${Uri.encode(bookTitle.orEmpty())}" +
                    "&$AUTHOR=${Uri.encode(author.orEmpty())}" +
                    "&$CATEGORY=${category?.name.orEmpty()}"
        }
    }

    object AddQuestion :
        AppDestinations(
            route = "add_question",
            routeWithArgs = "add_question/{$QUIZ_ID}/{$CHAPTER_NUMBER}"
        ) {
        fun createRoute(quizId: String, chapterNumber: Int) = "add_question/$quizId/$chapterNumber"
    }

    object ChapterQuestions : AppDestinations(
        route = "chapter_questions",
        routeWithArgs = "chapter_questions/{$QUIZ_ID}/{$CHAPTER_NUMBER}"
    ) {
        fun createRoute(quizId: String, chapterNumber: Int) = "chapter_questions/$quizId/$chapterNumber"
    }

    // The question is passed by its text, which identifies it within the chapter.
    object EditQuestion : AppDestinations(
        route = "edit_question",
        routeWithArgs = "edit_question/{$QUIZ_ID}/{$CHAPTER_NUMBER}?$QUESTION={$QUESTION}"
    ) {
        fun createRoute(quizId: String, chapterNumber: Int, question: String) =
            "edit_question/$quizId/$chapterNumber?$QUESTION=${Uri.encode(question)}"
    }

    companion object {
        const val QUESTION = "question"
        const val QUIZ_ID = "quizId"
        const val CHAPTER_NUMBER = "chapterNumber"
        const val BOOK_TITLE = "bookTitle"
        const val AUTHOR = "author"
        const val CATEGORY = "category"
    }
}
