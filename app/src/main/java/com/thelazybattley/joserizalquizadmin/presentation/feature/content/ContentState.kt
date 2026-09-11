package com.thelazybattley.joserizalquizadmin.presentation.feature.content

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz

data class ContentState(
    val isLoading: Boolean = false,
    val quiz: List<Quiz> = emptyList()
) : BaseState
