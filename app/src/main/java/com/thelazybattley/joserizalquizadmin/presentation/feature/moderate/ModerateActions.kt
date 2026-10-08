package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import com.thelazybattley.joserizalquizadmin.base.BaseActions
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback

sealed class ModerateActions: BaseActions {

    data class SelectFeedbackType(val feedbackType: ModerateContentFeedback): ModerateActions()

    data class Retry(val feedbackType: ModerateContentFeedback) : ModerateActions()

    data class NavigateDestination(val destination: ModerateDestinations?) : ModerateActions()

    data class RequestRemoveSuggestion(val suggestedBook: RankedSuggestedBook) : ModerateActions()

    object CancelRemoveSuggestion : ModerateActions()

    object ConfirmRemoveSuggestion : ModerateActions()

    data class DismissReport(val reportedQuestion: RankedReportedQuestion) : ModerateActions()

    object Undo : ModerateActions()

    object SnackbarDismissed : ModerateActions()

    // Sent when the tab comes back into view, e.g. after fixing a question from a report.
    object Resumed : ModerateActions()

}
