package com.thelazybattley.joserizalquizadmin.data.model.quiz

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.util.Category
import kotlinx.serialization.Serializable

@Serializable
data class QuizDto(
    val author: String,
    val bookName: String,
    val category: String,
    val id: String,
    val chapters: List<ChapterDto>
)


fun QuizDto.toDomain(): Quiz {
    return Quiz(
        subtitle = author,
        title = bookName,
        category = Category.fromString(value = category),
        id = id,
        chapters = chapters.map { it.toDomain() }
    )
}
