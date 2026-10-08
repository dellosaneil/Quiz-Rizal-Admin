package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

sealed class ModerateDestinations {
    data class AddBook(val bookTitle: String, val author: String) : ModerateDestinations()
    data class EditQuestion(val quizId: String, val chapterNumber: Int, val question: String) : ModerateDestinations()
}
