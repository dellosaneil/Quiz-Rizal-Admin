package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.presentation.util.Category

sealed class ContentDestinations {
    data class AddBook(val category: Category? = null) : ContentDestinations()
    data class AddQuestion(val chapterNumber: Int, val quizId: String) : ContentDestinations()
}
