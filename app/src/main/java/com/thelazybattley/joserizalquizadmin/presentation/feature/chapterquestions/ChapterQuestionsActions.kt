package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions

import com.thelazybattley.joserizalquizadmin.base.BaseActions

sealed class ChapterQuestionsActions : BaseActions {
    data class Navigate(val destination: ChapterQuestionsDestinations?) : ChapterQuestionsActions()

    // Sent each time the screen resumes, so tags drop after reports are cleared from Edit Question.
    data object RefreshReports : ChapterQuestionsActions()
}
