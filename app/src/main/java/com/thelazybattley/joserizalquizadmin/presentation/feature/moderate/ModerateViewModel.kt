package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReportedQuestionsUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchSuggestedBooksUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.RemoveSuggestedBookUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.RestoreSuggestedBooksUseCase
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateContentFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ModerateViewModel @Inject constructor(
    private val fetchSuggestedBooksUseCase: FetchSuggestedBooksUseCase,
    private val fetchReportedQuestionsUseCase: FetchReportedQuestionsUseCase,
    private val removeSuggestedBookUseCase: RemoveSuggestedBookUseCase,
    private val restoreSuggestedBooksUseCase: RestoreSuggestedBooksUseCase
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

            is ModerateActions.RequestRemoveSuggestion -> updateState(
                newState = state.value.copy(pendingRemoval = action.suggestedBook)
            )

            ModerateActions.CancelRemoveSuggestion -> updateState(
                newState = state.value.copy(pendingRemoval = null)
            )

            ModerateActions.ConfirmRemoveSuggestion -> removeSuggestion()

            ModerateActions.UndoRemoveSuggestion -> undoRemoveSuggestion()

            ModerateActions.SnackbarDismissed -> updateState(
                newState = state.value.copy(snackbar = null, lastRemoval = null)
            )
        }
    }

    private fun removeSuggestion() {
        val suggestedBook = state.value.pendingRemoval ?: return
        val suggestedBooks = (state.value.suggestedBooks as? FeedbackList.Loaded)?.items ?: return
        val index = suggestedBooks.indexOf(suggestedBook)
        // Remove from the list right away; put it back if the write fails.
        updateState(
            newState = state.value.copy(
                pendingRemoval = null,
                lastRemoval = null,
                snackbar = null,
                suggestedBooks = FeedbackList.Loaded(items = suggestedBooks - suggestedBook)
            )
        )
        viewModelScope.launch {
            runCatching {
                withContext(context = Dispatchers.IO) {
                    removeSuggestedBookUseCase(bookTitle = suggestedBook.bookTitle)
                }
            }.onSuccess { removedEntries ->
                updateState(
                    newState = state.value.copy(
                        lastRemoval = SuggestionRemoval(
                            suggestedBook = suggestedBook,
                            index = index,
                            removedEntries = removedEntries
                        ),
                        snackbar = ModerateSnackbar.Removed(bookTitle = suggestedBook.bookTitle)
                    )
                )
            }.onFailure {
                reinsertSuggestion(suggestedBook = suggestedBook, index = index)
                updateState(
                    newState = state.value.copy(
                        snackbar = ModerateSnackbar.RemoveFailed(bookTitle = suggestedBook.bookTitle)
                    )
                )
            }
        }
    }

    private fun undoRemoveSuggestion() {
        val removal = state.value.lastRemoval ?: return
        reinsertSuggestion(suggestedBook = removal.suggestedBook, index = removal.index)
        updateState(newState = state.value.copy(lastRemoval = null, snackbar = null))
        viewModelScope.launch {
            runCatching {
                withContext(context = Dispatchers.IO) {
                    restoreSuggestedBooksUseCase(suggestedBooks = removal.removedEntries)
                }
            }.onFailure {
                // The list no longer matches Firestore, so reload it.
                fetchSuggestedBooks()
            }
        }
    }

    private fun reinsertSuggestion(suggestedBook: RankedSuggestedBook, index: Int) {
        val suggestedBooks = (state.value.suggestedBooks as? FeedbackList.Loaded)?.items ?: return
        val updated = suggestedBooks.toMutableList().apply {
            add(index.coerceIn(minimumValue = 0, maximumValue = size), suggestedBook)
        }
        updateState(newState = state.value.copy(suggestedBooks = FeedbackList.Loaded(items = updated)))
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
