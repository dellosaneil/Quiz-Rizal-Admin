package com.thelazybattley.joserizalquizadmin.data.network.model.quiz

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuestionDto(
    val question: String = "",
    val answer: String = "",
    val choices: List<String> = emptyList(),
)

fun QuestionDto.toDomain() = Question(
    question = question,
    answer = answer,
    choices = choices
)
