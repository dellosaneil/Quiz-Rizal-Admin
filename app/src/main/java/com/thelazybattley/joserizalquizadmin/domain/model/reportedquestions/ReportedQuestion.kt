package com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions

data class ReportedQuestion(
    val quizId: String,
    val question: String,
    val reportedIssue: String,
    val chapterNumber: Int
)
