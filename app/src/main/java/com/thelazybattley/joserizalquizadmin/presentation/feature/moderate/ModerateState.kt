package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseState
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback

data class ModerateState(
    val selectedFeedback: ModerateContentFeedback = ModerateContentFeedback.SUGGESTED_BOOKS,
    val suggestedBooks: FeedbackList<RankedSuggestedBook> = FeedbackList.Loading,
    val reportedQuestions: FeedbackList<RankedReportedQuestion> = FeedbackList.Loading,
    val destination: ModerateDestinations? = null
) : BaseState

sealed interface FeedbackList<out T> {
    data object Loading : FeedbackList<Nothing>
    data object Failed : FeedbackList<Nothing>
    data class Loaded<T>(val items: List<T>) : FeedbackList<T>
}
