package com.thelazybattley.joserizalquizadmin.domain.usecase

import com.thelazybattley.joserizalquizadmin.domain.QuizRepository
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import javax.inject.Inject

class DismissReportedQuestionUseCase @Inject constructor(private val repository: QuizRepository) {

    suspend operator fun invoke(reportedQuestion: RankedReportedQuestion) = repository.removeReportedQuestion(
        quizId = reportedQuestion.quizId,
        chapterNumber = reportedQuestion.chapterNumber,
        question = reportedQuestion.question
    )

}
