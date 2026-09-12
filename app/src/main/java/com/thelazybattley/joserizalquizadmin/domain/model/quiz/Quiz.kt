package com.thelazybattley.joserizalquizadmin.domain.model.quiz

import com.thelazybattley.joserizalquizadmin.data.local.entity.QuizEntity
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

data class Quiz(
    val id: String,
    val title: String,
    val author: String,
    val chapters: List<Chapter>,
    val category: Category,
) {
    companion object {
        fun dummy(id: String = "quiz_content_id"): Quiz {
            return Quiz(
                id = id,
                title = "Quiz Title",
                author = "Quiz Subtitle",
                category = Category.LIFE_OF_RIZAL,
                chapters = listOf(
                    Chapter.dummy(chapterNumber = 1),
                    Chapter.dummy(chapterNumber = 2),
                    Chapter.dummy(chapterNumber = 3),
                )
            )
        }
    }
}
fun List<Quiz>.toEntity() = map {quiz ->
    QuizEntity(
        id = quiz.id,
        title = quiz.title,
        author = quiz.author,
        category = quiz.category.name,
        chapters = quiz.chapters.toEntity()
    )
}
