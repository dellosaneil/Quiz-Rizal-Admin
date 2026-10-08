package com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks

data class SuggestedBook(
    val author: String,
    val bookTitle: String,
    val school: String
)

// Suggestions for the same title are grouped by this key, ignoring case and surrounding spaces.
fun String.toSuggestionKey() = trim().lowercase()
