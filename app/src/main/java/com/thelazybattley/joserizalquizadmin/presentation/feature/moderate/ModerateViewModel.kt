package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate

import androidx.lifecycle.viewModelScope
import com.thelazybattley.joserizalquizadmin.base.BaseViewModel
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.toSuggestionKey
import com.thelazybattley.joserizalquizadmin.domain.usecase.DismissReportedQuestionUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchReportedQuestionsUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.FetchSuggestedBooksUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.GetAllQuizUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.RemoveSuggestedBookUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.RestoreReportedQuestionsUseCase
import com.thelazybattley.joserizalquizadmin.domain.usecase.RestoreSuggestedBooksUseCase
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
    private val restoreSuggestedBooksUseCase: RestoreSuggestedBooksUseCase,
    private val dismissReportedQuestionUseCase: DismissReportedQuestionUseCase,
    private val restoreReportedQuestionsUseCase: RestoreReportedQuestionsUseCase,
    private val getAllQuizUseCase: GetAllQuizUseCase
) : BaseViewModel<ModerateState, ModerateActions>(
    initialState = ModerateState()
), ModerateCallback {

    init {
        fetchSuggestedBooks()
        fetchReportedQuestions()
        viewModelScope.launch {
            getAllQuizUseCase().collect { library ->
                updateState(
                    newState = state.value.copy(
                        libraryTitleKeys = library.map { it.title.toSuggestionKey() }.toSet()
                    )
                )
            }
        }
    }

    override fun handleAction(action: ModerateActions) {
        when(action) {
            is ModerateActions.SelectFeedbackType -> updateState(
                newState = state.value.copy(selectedFeedback = action.feedbackType)
            )

            is ModerateActions.Retry -> when (action.feedbackType) {
                ModerateContentFeedback.SUGGESTED_BOOKS -> fetchSuggestedBooks()
                ModerateContentFeedback.REPORTED_QUESTIONS -> fetchReportedQuestions()
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

            is ModerateActions.DismissReport -> dismissReport(reportedQuestion = action.reportedQuestion)

            ModerateActions.Undo -> undo()

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
                        lastRemoval = ModerateRemoval.Suggestion(
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

    // No confirmation step: the snackbar's Undo puts the reports back.
    private fun dismissReport(reportedQuestion: RankedReportedQuestion) {
        val reportedQuestions = (state.value.reportedQuestions as? FeedbackList.Loaded)?.items ?: return
        val index = reportedQuestions.indexOf(reportedQuestion)
        if (index < 0) return
        updateState(
            newState = state.value.copy(
                lastRemoval = null,
                snackbar = null,
                reportedQuestions = FeedbackList.Loaded(items = reportedQuestions - reportedQuestion)
            )
        )
        viewModelScope.launch {
            runCatching {
                withContext(context = Dispatchers.IO) {
                    dismissReportedQuestionUseCase(reportedQuestion = reportedQuestion)
                }
            }.onSuccess { removedEntries ->
                updateState(
                    newState = state.value.copy(
                        lastRemoval = ModerateRemoval.Report(
                            reportedQuestion = reportedQuestion,
                            index = index,
                            removedEntries = removedEntries
                        ),
                        snackbar = ModerateSnackbar.ReportDismissed
                    )
                )
            }.onFailure {
                reinsertReport(reportedQuestion = reportedQuestion, index = index)
                updateState(newState = state.value.copy(snackbar = ModerateSnackbar.DismissFailed))
            }
        }
    }

    private fun undo() {
        val removal = state.value.lastRemoval ?: return
        updateState(newState = state.value.copy(lastRemoval = null, snackbar = null))
        when (removal) {
            is ModerateRemoval.Suggestion -> {
                reinsertSuggestion(suggestedBook = removal.suggestedBook, index = removal.index)
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

            is ModerateRemoval.Report -> {
                reinsertReport(reportedQuestion = removal.reportedQuestion, index = removal.index)
                viewModelScope.launch {
                    runCatching {
                        withContext(context = Dispatchers.IO) {
                            restoreReportedQuestionsUseCase(reportedQuestions = removal.removedEntries)
                        }
                    }.onFailure {
                        fetchReportedQuestions()
                    }
                }
            }
        }
    }

    private fun reinsertSuggestion(suggestedBook: RankedSuggestedBook, index: Int) {
        val suggestedBooks = (state.value.suggestedBooks as? FeedbackList.Loaded)?.items ?: return
        updateState(
            newState = state.value.copy(suggestedBooks = FeedbackList.Loaded(items = suggestedBooks.insertAt(index, suggestedBook)))
        )
    }

    private fun reinsertReport(reportedQuestion: RankedReportedQuestion, index: Int) {
        val reportedQuestions = (state.value.reportedQuestions as? FeedbackList.Loaded)?.items ?: return
        updateState(
            newState = state.value.copy(
                reportedQuestions = FeedbackList.Loaded(items = reportedQuestions.insertAt(index, reportedQuestion))
            )
        )
    }

    private fun <T> List<T>.insertAt(index: Int, item: T) = toMutableList().apply {
        add(index.coerceIn(minimumValue = 0, maximumValue = size), item)
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
