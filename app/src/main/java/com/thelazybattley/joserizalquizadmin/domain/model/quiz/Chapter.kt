package com.thelazybattley.quizrizal.domain.model.quiz

import com.thelazybattley.quizrizal.data.local.entity.quiz.ChapterEntity

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
                    )
                )
            )
    }
}

fun Chapter.toEntity() = ChapterEntity(
    chapterName = chapterName,
    chapterNumber = chapterNumber,
    questions = questions.map { it.toEntity() }
)
