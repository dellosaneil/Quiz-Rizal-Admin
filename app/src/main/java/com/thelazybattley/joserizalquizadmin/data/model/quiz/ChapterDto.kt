package com.thelazybattley.joserizalquizadmin.data.model.quiz

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChapterDto(
    @SerialName("chapterName") val name: String = "",
    val questions: List<QuestionDto> = emptyList(),
    @SerialName("chapterNumber") val number: Int = 0,
)

fun ChapterDto.toDomain() = Chapter(
    chapterName = name,
    questions = questions.map { it.toDomain() },
    chapterNumber = number
)
