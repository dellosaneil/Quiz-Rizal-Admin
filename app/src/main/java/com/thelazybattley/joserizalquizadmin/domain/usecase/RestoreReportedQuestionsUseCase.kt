package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.ReportedQuestion
import javax.inject.Inject

class RestoreReportedQuestionsUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(reportedQuestions: List<ReportedQuestion>) =
        repository.restoreReportedQuestions(reportedQuestions = reportedQuestions)

}
