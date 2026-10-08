package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.FeedbackList
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateSnackbar as ModerateSnackbarMessage
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateState
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun ModerateScreen(
    modifier: Modifier = Modifier,
    state: ModerateState,
    callbacks: ModerateCallback,
    navigate: (ModerateDestinations) -> Unit = {}
) {
    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            callbacks.handleAction(action = ModerateActions.NavigateDestination(destination = null))
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val removedMessage = state.snackbar?.let { snackbar ->
        when (snackbar) {
            is ModerateSnackbarMessage.Removed -> stringResource(id = R.string.removed_suggestion, snackbar.bookTitle)
            is ModerateSnackbarMessage.RemoveFailed -> stringResource(id = R.string.remove_suggestion_failed, snackbar.bookTitle)
        }
    }
    val undoLabel = stringResource(id = R.string.undo)
    LaunchedEffect(key1 = state.snackbar) {
        val snackbar = state.snackbar ?: return@LaunchedEffect
        val result = snackbarHostState.showSnackbar(
            message = removedMessage.orEmpty(),
            actionLabel = if (snackbar is ModerateSnackbarMessage.Removed) undoLabel else null,
            duration = SnackbarDuration.Long
        )
        callbacks.handleAction(
            action = when (result) {
                SnackbarResult.ActionPerformed -> ModerateActions.UndoRemoveSuggestion
                SnackbarResult.Dismissed -> ModerateActions.SnackbarDismissed
            }
        )
    }

    state.pendingRemoval?.let { suggestedBook ->
        ModerateRemoveSuggestionSheet(suggestedBook = suggestedBook, callbacks = callbacks)
    }

    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { snackbarData ->
                ModerateSnackbar(snackbarData = snackbarData)
            }
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            stickyHeader {
                ModerateSegmentedControl(
                    selectedFeedback = state.selectedFeedback,
                    callbacks = callbacks,
                )
            }
            when (state.selectedFeedback) {
                ModerateContentFeedback.SUGGESTED_BOOKS -> feedbackItems(
                    feedbackList = state.suggestedBooks,
                    content = FeedbackContent(
                        noteRes = R.string.sorted_by_how_many_students_requested,
                        loadingRes = R.string.loading_suggestions,
                        failedRes = R.string.failed_to_load_suggestions,
                        emptyGlyph = "◆",
                        emptyRes = R.string.no_suggested_books,
                        skeletonTitleWidths = listOf(0.5f, 0.6f, 0.45f, 0.55f),
                        skeletonSubtitleWidth = 0.35f
                    ),
                    key = { it.bookTitle.lowercase() }
                ) { index, suggestedBook ->
                    ModerateSuggestedBookCard(
                        modifier = Modifier.fillMaxWidth(),
                        rank = index + 1,
                        suggestedBook = suggestedBook,
                        callbacks = callbacks
                    )
                }

                ModerateContentFeedback.REPORTED_QUESTIONS -> feedbackItems(
                    feedbackList = state.reportedQuestions,
                    content = FeedbackContent(
                        noteRes = R.string.sorted_by_number_of_reports,
                        loadingRes = R.string.loading_reports,
                        failedRes = R.string.failed_to_load_reports,
                        emptyGlyph = "⚑",
                        emptyRes = R.string.no_reported_questions,
                        skeletonTitleWidths = listOf(0.7f, 0.65f, 0.72f),
                        skeletonSubtitleWidth = 0.4f
                    ),
                    key = { "${it.quizId}-${it.chapterNumber}-${it.question}" }
                ) { _, reportedQuestion ->
                    ModerateReportedQuestionCard(
                        modifier = Modifier.fillMaxWidth(),
                        reportedQuestion = reportedQuestion
                    )
                }
            }
        }
    }
}

private class FeedbackContent(
    @StringRes val noteRes: Int,
    @StringRes val loadingRes: Int,
    @StringRes val failedRes: Int,
    val emptyGlyph: String,
    @StringRes val emptyRes: Int,
    val skeletonTitleWidths: List<Float>,
    val skeletonSubtitleWidth: Float
)

private fun <T> LazyListScope.feedbackItems(
    feedbackList: FeedbackList<T>,
    content: FeedbackContent,
    key: (T) -> Any,
    itemContent: @Composable (index: Int, item: T) -> Unit
) {
    when (feedbackList) {
        FeedbackList.Loading -> {
            item { SectionNote(textRes = content.loadingRes) }
            items(items = content.skeletonTitleWidths) { titleWidth ->
                ModerateSkeletonRow(
                    modifier = Modifier.fillMaxWidth(),
                    titleWidthFraction = titleWidth,
                    subtitleWidthFraction = content.skeletonSubtitleWidth
                )
            }
        }

        FeedbackList.Failed -> item { SectionNote(textRes = content.failedRes) }

        is FeedbackList.Loaded if feedbackList.items.isEmpty() -> item {
            ModerateEmptyState(
                modifier = Modifier.fillMaxWidth(),
                glyph = content.emptyGlyph,
                messageRes = content.emptyRes
            )
        }

        is FeedbackList.Loaded -> {
            item { SectionNote(textRes = content.noteRes) }
            itemsIndexed(items = feedbackList.items, key = { _, item -> key(item) }) { index, feedback ->
                itemContent(index, feedback)
            }
        }
    }
}

@Composable
private fun SectionNote(@StringRes textRes: Int) {
    Text(
        modifier = Modifier.padding(bottom = 6.dp),
        text = stringResource(id = textRes),
        style = typography.regular12,
        color = colors.taupe
    )
}

private class ModerateStateProvider : PreviewParameterProvider<ModerateState> {
    override val values = sequenceOf(
        ModerateState(
            suggestedBooks = FeedbackList.Loaded(
                items = listOf(
                    RankedSuggestedBook.dummy(),
                    RankedSuggestedBook.dummy(bookTitle = "Sa Aking Mga Kabata", requestCount = 19)
                )
            )
        ),
        ModerateState(),
        ModerateState(suggestedBooks = FeedbackList.Loaded(items = emptyList())),
        ModerateState(
            selectedFeedback = ModerateContentFeedback.REPORTED_QUESTIONS,
            reportedQuestions = FeedbackList.Loaded(
                items = listOf(RankedReportedQuestion.dummy(), RankedReportedQuestion.dummy(reportCount = 8))
            )
        ),
        ModerateState(selectedFeedback = ModerateContentFeedback.REPORTED_QUESTIONS)
    )
}

@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(ModerateStateProvider::class) state: ModerateState) {
    AppTheme {
        ModerateScreen(
            modifier = Modifier,
            state = state,
            callbacks = ModerateCallback.default()
        )
    }
}
