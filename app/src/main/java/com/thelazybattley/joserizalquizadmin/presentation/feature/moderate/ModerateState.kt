package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.SuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback

data class ModerateState(
    val selectedFeedback: ModerateContentFeedback = ModerateContentFeedback.SUGGESTED_BOOKS,
    val suggestedBooks: FeedbackList<RankedSuggestedBook> = FeedbackList.Loading,
    val reportedQuestions: FeedbackList<RankedReportedQuestion> = FeedbackList.Loading,
    val destination: ModerateDestinations? = null,
    // Suggestion awaiting confirmation in the remove sheet.
    val pendingRemoval: RankedSuggestedBook? = null,
    // Last removal, kept so the snackbar's Undo can restore it.
    val lastRemoval: SuggestionRemoval? = null,
    val snackbar: ModerateSnackbar? = null
) : BaseState

data class SuggestionRemoval(
    val suggestedBook: RankedSuggestedBook,
    val index: Int,
    val removedEntries: List<SuggestedBook>
)

sealed interface ModerateSnackbar {
    val bookTitle: String

    data class Removed(override val bookTitle: String) : ModerateSnackbar
    data class RemoveFailed(override val bookTitle: String) : ModerateSnackbar
}

sealed interface FeedbackList<out T> {
    data object Loading : FeedbackList<Nothing>
    data object Failed : FeedbackList<Nothing>
    data class Loaded<T>(val items: List<T>) : FeedbackList<T>
}
