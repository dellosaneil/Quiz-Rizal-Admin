package com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks

data class RankedSuggestedBook(
    val bookTitle: String,
    val author: String,
    val requestCount: Int
) {
    companion object {
        fun dummy(bookTitle: String = "El Filibusterismo", requestCount: Int = 34) = RankedSuggestedBook(
            bookTitle = bookTitle,
            author = "José Rizal",
            requestCount = requestCount
        )
    }
}
