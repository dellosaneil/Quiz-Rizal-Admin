package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class FetchReportedQuestionsUseCase @Inject constructor(private val repository: QuizRepository) {

    // Groups reports for the same question, most reported first.
    suspend operator fun invoke(): List<RankedReportedQuestion> = coroutineScope {
        val reports = async { repository.fetchReportedQuestions() }
        val bookTitles = async {
            repository.fetchQuizContent().associate { quiz -> quiz.id to quiz.title }
        }
        reports.await()
            .groupBy { Triple(it.quizId, it.chapterNumber, it.question) }
            .map { (key, reports) ->
                val (quizId, chapterNumber, question) = key
                RankedReportedQuestion(
                    quizId = quizId,
                    bookTitle = bookTitles.await()[quizId],
                    chapterNumber = chapterNumber,
                    question = question,
                    reasons = reports
                        .map { it.reportedIssue.trim() }
                        .filter { it.isNotEmpty() }
                        .groupingBy { it }
                        .eachCount()
                        .entries
                        .sortedByDescending { it.value }
                        .map { it.key },
                    reportCount = reports.size
                )
            }
            .sortedByDescending { it.reportCount }
    }
}
