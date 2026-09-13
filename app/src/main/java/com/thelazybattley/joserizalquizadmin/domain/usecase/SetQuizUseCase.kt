package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import javax.inject.Inject

class SetQuizUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(
        author: String,
        bookName: String,
        category: String,
        chapters: List<String>
    ): String = repository.setQuiz(
        author = author,
        bookName = bookName,
        category = category,
        chapters = chapters
    )
}
