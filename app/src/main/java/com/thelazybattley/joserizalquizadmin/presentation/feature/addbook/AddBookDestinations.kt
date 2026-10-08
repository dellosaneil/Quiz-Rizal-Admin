package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

sealed class AddBookDestinations {
    data object Back : AddBookDestinations()
    data object AddAnotherBook : AddBookDestinations()
    data class AddQuestion(val quizId: String) : AddBookDestinations()
}
