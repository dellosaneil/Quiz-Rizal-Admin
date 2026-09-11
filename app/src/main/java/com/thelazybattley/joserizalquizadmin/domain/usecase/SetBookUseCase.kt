package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import javax.inject.Inject

class SetBookUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(
        author: String,
        bookName: String,
        category: String,
        chapters: List<String>
    ) = repository.setBook(
        author = author,
        bookName = bookName,
        category = category,
        chapters = chapters
    )
}
