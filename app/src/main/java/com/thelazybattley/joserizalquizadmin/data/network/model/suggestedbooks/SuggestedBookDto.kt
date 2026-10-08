package com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks

import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Matches BookSuggestionDto written by the quiz app into quiz/{env}/feedback/suggested_book.
@Serializable
data class SuggestedBookDto(
    @SerialName("book_title") val bookTitle: String = "",
    @SerialName("author") val author: String = "",
    @SerialName("school") val school: String = ""
)

fun SuggestedBookDto.toDomain() = SuggestedBook(
    author = author,
    bookTitle = bookTitle,
    school = school
)

fun SuggestedBook.toDto() = SuggestedBookDto(
    bookTitle = bookTitle,
    author = author,
    school = school
)
