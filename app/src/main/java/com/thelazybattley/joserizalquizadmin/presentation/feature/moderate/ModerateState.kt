package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.ReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.toSuggestionKey
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback

data class ModerateState(
    val selectedFeedback: ModerateContentFeedback = ModerateContentFeedback.SUGGESTED_BOOKS,
    val suggestedBooks: FeedbackList<RankedSuggestedBook> = FeedbackList.Loading,
    val reportedQuestions: FeedbackList<RankedReportedQuestion> = FeedbackList.Loading,
    // Titles of books already in the local library, as suggestion keys.
    val libraryTitleKeys: Set<String> = emptySet(),
    val destination: ModerateDestinations? = null,
    // Suggestion awaiting confirmation in the remove sheet.
    val pendingRemoval: RankedSuggestedBook? = null,
    // Last removal or dismissal, kept so the snackbar's Undo can restore it.
    val lastRemoval: ModerateRemoval? = null,
    val snackbar: ModerateSnackbar? = null
) : BaseState {

    fun isInLibrary(suggestedBook: RankedSuggestedBook) =
        suggestedBook.bookTitle.toSuggestionKey() in libraryTitleKeys
}

sealed interface ModerateRemoval {
    val index: Int

    data class Suggestion(
        val suggestedBook: RankedSuggestedBook,
        override val index: Int,
        val removedEntries: List<SuggestedBook>
    ) : ModerateRemoval

    data class Report(
        val reportedQuestion: RankedReportedQuestion,
        override val index: Int,
        val removedEntries: List<ReportedQuestion>
    ) : ModerateRemoval
}

sealed interface ModerateSnackbar {
    data class Removed(val bookTitle: String) : ModerateSnackbar
    data class RemoveFailed(val bookTitle: String) : ModerateSnackbar
    data object ReportDismissed : ModerateSnackbar
    data object DismissFailed : ModerateSnackbar
}

sealed interface FeedbackList<out T> {
    data object Loading : FeedbackList<Nothing>
    data object Failed : FeedbackList<Nothing>
    data class Loaded<T>(val items: List<T>) : FeedbackList<T>
}
