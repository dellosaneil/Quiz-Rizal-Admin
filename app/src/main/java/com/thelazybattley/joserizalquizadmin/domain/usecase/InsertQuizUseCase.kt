package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import javax.inject.Inject

class InsertQuizUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(quiz: List<Quiz>) = repository.insertQuiz(quiz = quiz)

}
