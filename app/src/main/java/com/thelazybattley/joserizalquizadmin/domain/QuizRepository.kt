package com.thelazybattley.joserizalquizadmin.domain

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import kotlinx.coroutines.flow.Flow

interface QuizRepository {

    suspend fun fetchQuizContent(): List<Quiz>

    fun setQuiz(
        author: String,
        bookName: String,
        category: String,
        chapters: List<String>
    ): String

    fun setUpdatedQuiz(quiz: Quiz)

    fun getAllQuiz(): Flow<List<Quiz>>

    suspend fun insertQuiz(quiz: List<Quiz>)

    suspend fun getQuizById(id: String): Quiz
}
