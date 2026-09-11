package com.thelazybattley.joserizalquizadmin.domain

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz

interface QuizRepository {

    suspend fun fetchQuizContent(): List<Quiz>

    fun setBook(
        author: String,
        bookName: String,
        category: String,
        chapters: List<String>
    )
}
