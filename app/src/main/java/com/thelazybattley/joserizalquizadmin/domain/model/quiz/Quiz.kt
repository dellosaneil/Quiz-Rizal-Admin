package com.thelazybattley.quizrizal.domain.model.quiz

import com.thelazybattley.quizrizal.data.local.entity.quiz.QuizEntity
import com.thelazybattley.quizrizal.domain.model.ContentCategory

data class Quiz(
    val id: String,
    val title: String,
    val subtitle: String,
    val chapters: List<Chapter>,
    val category: ContentCategory,
) {
    companion object {
        fun dummy(id: String = "quiz_content_id"): Quiz {
            return Quiz(
                id = id,
                title = "Quiz Title",
                subtitle = "Quiz Subtitle",
                category = ContentCategory.LIFE_OF_RIZAL,
                chapters = listOf(
                    Chapter.dummy(chapterNumber = 1),
                    Chapter.dummy(chapterNumber = 2),
                    Chapter.dummy(chapterNumber = 3),
                )
            )
        }
    }
}


fun Quiz.toEntity() =
    QuizEntity(
        id = id,
        title = title,
        author = subtitle,
        category = category.name,
        chapters = chapters.map { it.toEntity() }
    )
