package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import javax.inject.Inject

class RemoveSuggestedBookUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(bookTitle: String) = repository.removeSuggestedBook(bookTitle = bookTitle)

}
