package com.thelazybattley.joserizalquizadmin.domain.model.release

data class BuildSummary(
    val bookCount: Int,
    val questionCount: Int
)

data class ReleaseOverview(
    val debug: BuildSummary,
    val release: BuildSummary,
    val changes: List<ReleaseChange>
) {
    companion object {
        fun dummy() = ReleaseOverview(
            debug = BuildSummary(bookCount = 3, questionCount = 15),
            release = BuildSummary(bookCount = 2, questionCount = 11),
            changes = listOf(
                ReleaseChange.BookAdded(bookTitle = "El Filibusterismo", chapterCount = 1),
                ReleaseChange.QuestionEdited(
                    bookTitle = "Noli Me Tangere",
                    chapterNumber = 1,
                    question = "What does Ibarra vow to do after learning of his father's fate?",
                    choicesChanged = true,
                    answerChanged = false
                ),
                ReleaseChange.QuestionRemoved(
                    bookTitle = "Noli Me Tangere",
                    chapterNumber = 1,
                    question = "Who hosts the reunion party that opens the novel?"
                )
            )
        )
    }
}
