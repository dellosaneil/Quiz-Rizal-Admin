package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz

data class AddQuestionState(
    val isLoading: Boolean = false,
    val quizId: String = "",
    val chapterNumber: Int = -1,
    val quiz: Quiz? = null,
    val correctAnswerIndex: Int = -1,
    val choices: List<String> = listOf("", "", "", ""),
    val destination: AddQuestionDestinations? = null,
    val question: String = "",
    val isButtonEnabled: Boolean = false,
    val showSuccessBanner: Boolean = false
) : BaseState
