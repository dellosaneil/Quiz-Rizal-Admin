package com.thelazybattley.joserizalquizadmin.data.network.model.suggestedbooks

import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook

data class SuggestedBookDto(
    val id: String,
    val author: String,
    val bookTitle: String,
    val school: String
)

fun SuggestedBookDto.toDomain() = SuggestedBook(
    id = id,
    author = author,
    bookTitle = bookTitle,
    school = school
)
