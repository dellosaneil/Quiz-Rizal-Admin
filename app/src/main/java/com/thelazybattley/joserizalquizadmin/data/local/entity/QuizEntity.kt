package com.thelazybattley.joserizalquizadmin.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.util.Category
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class QuizEntity(
    val author: String,
    val title: String,
    val category: String,
    @PrimaryKey val id: String,
    val chapters: List<ChapterEntity>
)


fun QuizEntity.toDomain(): Quiz {
    return Quiz(
        author = author,
        title = title,
        category = Category.fromString(value = category),
        id = id,
        chapters = chapters.map { it.toDomain() }
    )
}

