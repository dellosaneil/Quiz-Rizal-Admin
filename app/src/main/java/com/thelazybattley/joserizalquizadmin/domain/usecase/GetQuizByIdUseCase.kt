package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import javax.inject.Inject

class GetQuizByIdUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(id: String) = repository.getQuizById(id = id)

}
