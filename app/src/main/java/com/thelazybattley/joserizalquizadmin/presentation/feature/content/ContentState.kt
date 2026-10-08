package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

data class ContentState(
    val isLoading: Boolean = true,
    val quiz: List<Quiz> = emptyList(),
    // Null shows every category.
    val categoryFilter: Category? = null,
    val expandedBook: String? = null,
    val destination: ContentDestinations? = null
) : BaseState {

    val visibleQuiz: List<Quiz>
        get() = categoryFilter?.let { category -> quiz.filter { it.category == category } } ?: quiz
}
