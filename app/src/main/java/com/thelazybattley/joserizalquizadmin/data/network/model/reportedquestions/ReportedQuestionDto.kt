package com.thelazybattley.joserizalquizadmin.data.network.model.reportedquestions

import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.ReportedQuestion
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Matches DisputeAnswerDto written by the quiz app into quiz/{env}/feedback/dispute_answer.
@Serializable
data class ReportedQuestionDto(
    @SerialName("quiz_id") val quizId: String = "",
    @SerialName("question") val question: String = "",
    @SerialName("reported_issue") val reportedIssue: String = "",
    @SerialName("chapter_number") val chapterNumber: Int = 0
)

fun ReportedQuestion.toDto() = ReportedQuestionDto(
    quizId = quizId,
    question = question,
    reportedIssue = reportedIssue,
    chapterNumber = chapterNumber
)

fun ReportedQuestionDto.toDomain() = ReportedQuestion(
    quizId = quizId,
    question = question,
    reportedIssue = reportedIssue,
    chapterNumber = chapterNumber
)
