package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import javax.inject.Inject

class SetQuizContentToReleaseUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke() = repository.setQuizContentToRelease()

}
