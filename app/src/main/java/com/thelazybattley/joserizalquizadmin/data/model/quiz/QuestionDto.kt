package com.thelazybattley.joserizalquizadmin.data.model.quiz

import com.thelazybattley.quizrizal.domain.model.quiz.Question
import kotlinx.serialization.Serializable

@Serializable
data class BookQuestionDto(
    val question: String = "",
    val answer: String = "",
    val choices: List<String> = emptyList(),
)

fun BookQuestionDto.toDomain() = Question(
    question = question,
    answer = answer,
    choices = choices
)
