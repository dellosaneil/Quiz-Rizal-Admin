package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import javax.inject.Inject

class GetAllQuizUseCase @Inject constructor(private val repository: QuizRepository) {

    operator fun invoke() = repository.getAllQuiz()

}
