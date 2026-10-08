package com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions

data class RankedReportedQuestion(
    val quizId: String,
    val bookTitle: String?,
    val chapterNumber: Int,
    val question: String,
    // Distinct issues students picked, most common first.
    val reasons: List<String>,
    val reportCount: Int
) {
    companion object {
        fun dummy(reportCount: Int = 14) = RankedReportedQuestion(
            quizId = "quiz_content_id",
            bookTitle = "Noli Me Tangere",
            chapterNumber = 1,
            question = "What does Ibarra vow to do after learning of his father's fate?",
            reasons = listOf("Answer key seems wrong"),
            reportCount = reportCount
        )
    }
}
