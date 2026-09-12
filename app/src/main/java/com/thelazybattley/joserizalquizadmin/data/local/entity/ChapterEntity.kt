package com.thelazybattley.joserizalquizadmin.data.local.entity

import androidx.room.Entity
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class ChapterEntity(
    val name: String,
    val questions: List<QuestionEntity>,
    val number: Int,
)

fun ChapterEntity.toDomain() = Chapter(
    chapterName = name,
    questions = questions.map { it.toDomain() },
    chapterNumber = number
)
