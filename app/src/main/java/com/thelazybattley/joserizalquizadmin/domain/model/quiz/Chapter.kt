package com.thelazybattley.joserizalquizadmin.domain.model.quiz

import com.thelazybattley.joserizalquizadmin.data.local.entity.ChapterEntity

data class Chapter(
    val chapterName: String,
    val chapterNumber: Int,
    val questions: List<Question>
) {
    companion object {
        fun dummy(chapterNumber: Int) =
            Chapter(
                chapterName = "Chapter Name", chapterNumber = chapterNumber,
                questions = listOf(
                    Question(
                        question = "Test Question",
                        choices = listOf("Choice 1", "Choice 2", "Choice 3"),
                        answer = "Choice 1"
                    ),
                    Question(
                        question = "Test Question 1",
                        choices = listOf("Choice 1", "Choice 2", "Choice 3"),
                        answer = "Choice 1"
                    ),
                )
            )
    }
}

fun List<Chapter>.getTotalQuestions() = sumOf { it.questions.size }

fun List<Chapter>.toEntity() = map { ChapterEntity(
    name = it.chapterName,
    questions = it.questions.toEntity(),
    number = it.chapterNumber
) }
