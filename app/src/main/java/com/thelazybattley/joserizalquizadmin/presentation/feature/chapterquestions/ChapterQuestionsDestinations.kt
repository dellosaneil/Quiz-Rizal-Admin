package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions

sealed class ChapterQuestionsDestinations {
    data object Back : ChapterQuestionsDestinations()
    data object AddQuestion : ChapterQuestionsDestinations()
    data class EditQuestion(val question: String) : ChapterQuestionsDestinations()
}
