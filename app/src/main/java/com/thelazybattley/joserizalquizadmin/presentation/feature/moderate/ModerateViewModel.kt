package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReportedQuestionsUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchSuggestedBooksUseCase
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ModerateViewModel @Inject constructor(
    private val fetchSuggestedBooksUseCase: FetchSuggestedBooksUseCase,
    private val fetchReportedQuestionsUseCase: FetchReportedQuestionsUseCase
) : BaseViewModel<ModerateState, ModerateActions>(
    initialState = ModerateState()
), ModerateCallback {

    init {
        fetchSuggestedBooks()
        fetchReportedQuestions()
    }

    override fun handleAction(action: ModerateActions) {
        when(action) {
            is ModerateActions.SelectFeedbackType -> {
                updateState(newState = state.value.copy(selectedFeedback = action.feedbackType))
                // Switching to a tab that failed to load retries it.
                when (action.feedbackType) {
                    ModerateContentFeedback.SUGGESTED_BOOKS -> if (state.value.suggestedBooks is FeedbackList.Failed) {
                        fetchSuggestedBooks()
                    }

                    ModerateContentFeedback.REPORTED_QUESTIONS -> if (state.value.reportedQuestions is FeedbackList.Failed) {
                        fetchReportedQuestions()
                    }
                }
            }

            is ModerateActions.NavigateDestination -> updateState(
                newState = state.value.copy(destination = action.destination)
            )
        }
    }

    private fun fetchSuggestedBooks() {
        updateState(newState = state.value.copy(suggestedBooks = FeedbackList.Loading))
        // Fetch off the main thread, but update state on it so the two lists can't overwrite each other.
        viewModelScope.launch {
            val suggestedBooks = runCatching { withContext(context = Dispatchers.IO) { fetchSuggestedBooksUseCase() } }
                .fold(onSuccess = { FeedbackList.Loaded(items = it) }, onFailure = { FeedbackList.Failed })
            updateState(newState = state.value.copy(suggestedBooks = suggestedBooks))
        }
    }

    private fun fetchReportedQuestions() {
        updateState(newState = state.value.copy(reportedQuestions = FeedbackList.Loading))
        // Fetch off the main thread, but update state on it so the two lists can't overwrite each other.
        viewModelScope.launch {
            val reportedQuestions = runCatching { withContext(context = Dispatchers.IO) { fetchReportedQuestionsUseCase() } }
                .fold(onSuccess = { FeedbackList.Loaded(items = it) }, onFailure = { FeedbackList.Failed })
            updateState(newState = state.value.copy(reportedQuestions = reportedQuestions))
        }
    }
}
