package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
import javax.inject.Inject

class RestoreSuggestedBooksUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(suggestedBooks: List<SuggestedBook>) =
        repository.restoreSuggestedBooks(suggestedBooks = suggestedBooks)

}
