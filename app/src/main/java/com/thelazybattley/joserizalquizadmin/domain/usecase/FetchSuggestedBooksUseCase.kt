package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.toSuggestionKey
import javax.inject.Inject

class FetchSuggestedBooksUseCase @Inject constructor(private val repository: QuizRepository) {

    // Groups suggestions for the same title (ignoring case and spacing), most requested first.
    suspend operator fun invoke(): List<RankedSuggestedBook> = repository.fetchSuggestedBooks()
        .filter { it.bookTitle.isNotBlank() }
        .groupBy { it.bookTitle.toSuggestionKey() }
        .values
        .map { suggestions ->
            RankedSuggestedBook(
                bookTitle = suggestions.first().bookTitle.trim(),
                author = suggestions
                    .map { it.author.trim() }
                    .filter { it.isNotEmpty() }
                    .groupingBy { it }
                    .eachCount()
                    .maxByOrNull { it.value }
                    ?.key
                    .orEmpty(),
                requestCount = suggestions.size
            )
        }
        .sortedWith(compareByDescending<RankedSuggestedBook> { it.requestCount }.thenBy { it.bookTitle })
}
