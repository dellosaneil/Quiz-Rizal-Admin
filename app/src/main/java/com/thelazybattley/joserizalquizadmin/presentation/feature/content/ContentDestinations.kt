package com.thelazybattley.joserizalquizadmin.presentation.feature.content

sealed class ContentDestinations {
    object AddBook : ContentDestinations()
    data class AddQuestion(val chapterNumber: Int, val quizId: String) : ContentDestinations()
}
