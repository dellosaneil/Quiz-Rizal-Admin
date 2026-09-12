package com.thelazybattley.joserizalquizadmin.domain.model.quiz

import com.thelazybattley.joserizalquizadmin.data.local.entity.QuestionEntity

data class Question(
    val question: String,
    val choices: List<String>,
    val answer: String
) {
    companion object {
        fun dummy() = Question(
            question = "What is a question?",
            choices = listOf("Choice 1", "Choice 2", "Choice 3"),
            answer = "Choice 1"
        )
    }
}

fun List<Question>.toEntity() = map { question ->
    QuestionEntity(
        question = question.question,
        choices = question.choices,
        answer = question.answer
    )
}
