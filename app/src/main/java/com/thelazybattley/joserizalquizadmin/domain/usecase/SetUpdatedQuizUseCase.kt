package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import javax.inject.Inject

class SetUpdatedQuizUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(quiz: Quiz) = repository.setUpdatedQuiz(quiz = quiz)

}
