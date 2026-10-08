package com.thelazybattley.joserizalquizadmin.domain

import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.ReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
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

    fun getQuizById(id: String): Flow<Quiz>

    suspend fun fetchSuggestedBooks(): List<SuggestedBook>

    suspend fun fetchReportedQuestions(): List<ReportedQuestion>

    // Removes every suggestion for this title and returns the removed entries.
    suspend fun removeSuggestedBook(bookTitle: String): List<SuggestedBook>

    suspend fun restoreSuggestedBooks(suggestedBooks: List<SuggestedBook>)

    suspend fun setQuizContentToRelease()
}
