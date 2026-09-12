package com.thelazybattley.joserizalquizadmin.data.local.entity

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import kotlinx.serialization.Serializable

@Serializable
data class QuestionEntity(
    val question: String,
    val answer: String,
    val choices: List<String>,
)

fun QuestionEntity.toDomain() = Question(
    question = question,
    answer = answer,
    choices = choices
)
