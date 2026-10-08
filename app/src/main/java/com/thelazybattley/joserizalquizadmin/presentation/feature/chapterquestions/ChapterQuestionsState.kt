package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz

data class ChapterQuestionsState(
    val quizId: String = "",
    val chapterNumber: Int = -1,
    val quiz: Quiz? = null,
    // Report counts keyed by question text, for this chapter only.
    val reportCounts: Map<String, Int> = emptyMap(),
    val destination: ChapterQuestionsDestinations? = null
) : BaseState {

    val chapter: Chapter?
        get() = quiz?.chapters?.firstOrNull { it.chapterNumber == chapterNumber }
}
