package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

data class AddBookState(
    val category: Category = Category.LIFE_OF_RIZAL,
    val author: String = "",
    val title: String = "",
    val chapters: List<String> = listOf(element = ""),
    val isFromSuggestion: Boolean = false,
    val publishPhase: PublishPhase = PublishPhase.EDITING,
    val publishFailed: Boolean = false,
    val destination: AddBookDestinations? = null
) : BaseState {

    val blankChapterCount: Int
        get() = chapters.count { it.isBlank() }

    // What still stops the book from being published, shown above the publish button.
    val blocker: PublishBlocker?
        get() = when {
            title.isBlank() || author.isBlank() -> PublishBlocker.MISSING_DETAILS
            blankChapterCount > 0 -> PublishBlocker.BLANK_CHAPTERS
            else -> null
        }
}

enum class PublishBlocker {
    MISSING_DETAILS,
    BLANK_CHAPTERS
}

sealed interface PublishPhase {
    data object EDITING : PublishPhase
    data object PUBLISHING : PublishPhase
    data class Published(val quizId: String) : PublishPhase
}
