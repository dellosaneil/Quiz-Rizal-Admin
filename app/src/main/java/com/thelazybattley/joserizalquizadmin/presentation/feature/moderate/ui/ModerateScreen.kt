package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.FeedbackList
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateState
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseBannerType
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseStatusBanner
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonFilterChip
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateSnackbar as ModerateSnackbarMessage

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
    val snackbarMessage = state.snackbar?.let { snackbar ->
        when (snackbar) {
            is ModerateSnackbarMessage.Removed -> stringResource(id = R.string.removed_suggestion, snackbar.bookTitle)
            is ModerateSnackbarMessage.RemoveFailed -> stringResource(id = R.string.remove_suggestion_failed, snackbar.bookTitle)
            ModerateSnackbarMessage.ReportDismissed -> stringResource(id = R.string.report_dismissed)
            ModerateSnackbarMessage.DismissFailed -> stringResource(id = R.string.dismiss_report_failed)
        }
    }
    val undoLabel = stringResource(id = R.string.undo)
    LaunchedEffect(key1 = state.snackbar) {
        val snackbar = state.snackbar ?: return@LaunchedEffect
        val canUndo = snackbar is ModerateSnackbarMessage.Removed || snackbar is ModerateSnackbarMessage.ReportDismissed
        val result = snackbarHostState.showSnackbar(
            message = snackbarMessage.orEmpty(),
            actionLabel = if (canUndo) undoLabel else null,
            duration = SnackbarDuration.Long
        )
        callbacks.handleAction(
            action = when (result) {
                SnackbarResult.ActionPerformed -> ModerateActions.Undo
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
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            item {
                ModerateHeader(modifier = Modifier.fillMaxWidth(), state = state)
            }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(state = rememberScrollState())
                        .selectableGroup(),
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
                ) {
                    ModerateContentFeedback.entries.forEach { feedback ->
                        CommonFilterChip(
                            label = stringResource(id = feedback.id),
                            count = when (feedback) {
                                ModerateContentFeedback.SUGGESTED_BOOKS -> state.suggestedBooks.count
                                ModerateContentFeedback.REPORTED_QUESTIONS -> state.reportedQuestions.count
                            },
                            isSelected = state.selectedFeedback == feedback
                        ) {
                            callbacks.handleAction(action = ModerateActions.SelectFeedbackType(feedbackType = feedback))
                        }
                    }
                }
            }
            when (state.selectedFeedback) {
                ModerateContentFeedback.SUGGESTED_BOOKS -> feedbackItems(
                    feedbackList = state.suggestedBooks,
                    content = FeedbackContent(
                        noteRes = R.string.most_requested_first,
                        failedTitleRes = R.string.failed_to_load_suggestions,
                        emptyTitleRes = R.string.no_suggested_books_title,
                        emptyBodyRes = R.string.no_suggested_books,
                        skeletonTitleWidths = listOf(0.5f, 0.6f, 0.45f, 0.55f),
                        skeletonSubtitleWidth = 0.35f
                    ),
                    onRetry = {
                        callbacks.handleAction(action = ModerateActions.Retry(feedbackType = ModerateContentFeedback.SUGGESTED_BOOKS))
                    },
                    key = { it.bookTitle.lowercase() }
                ) { index, suggestedBook ->
                    ModerateSuggestedBookCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        rank = index + 1,
                        suggestedBook = suggestedBook,
                        isInLibrary = state.isInLibrary(suggestedBook = suggestedBook),
                        callbacks = callbacks
                    )
                }

                ModerateContentFeedback.REPORTED_QUESTIONS -> feedbackItems(
                    feedbackList = state.reportedQuestions,
                    content = FeedbackContent(
                        noteRes = R.string.most_reported_first,
                        failedTitleRes = R.string.failed_to_load_reports,
                        emptyTitleRes = R.string.no_reported_questions_title,
                        emptyBodyRes = R.string.no_reported_questions,
                        skeletonTitleWidths = listOf(0.7f, 0.65f, 0.72f),
                        skeletonSubtitleWidth = 0.4f
                    ),
                    onRetry = {
                        callbacks.handleAction(action = ModerateActions.Retry(feedbackType = ModerateContentFeedback.REPORTED_QUESTIONS))
                    },
                    key = { "${it.quizId}-${it.chapterNumber}-${it.question}" }
                ) { _, reportedQuestion ->
                    ModerateReportedQuestionCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        reportedQuestion = reportedQuestion,
                        callbacks = callbacks
                    )
                }
            }
        }
    }
}

private val FeedbackList<*>.count: Int?
    get() = (this as? FeedbackList.Loaded)?.items?.size

@Composable
private fun ModerateHeader(modifier: Modifier = Modifier, state: ModerateState) {
    Column(
        modifier = modifier.padding(bottom = 4.dp),
        verticalArrangement = Arrangement.spacedBy(space = 2.dp)
    ) {
        Text(
            modifier = Modifier.semantics { heading() },
            text = stringResource(id = R.string.moderate),
            style = typography.bold16.copy(fontSize = 18.sp),
            color = colors.espresso
        )
        val suggestions = state.suggestedBooks.count
        val reports = state.reportedQuestions.count
        // Shown once both lists have loaded.
        if (suggestions != null && reports != null) {
            Text(
                text = stringResource(
                    id = R.string.dot_separated,
                    pluralStringResource(id = R.plurals.suggestion_count, count = suggestions, suggestions),
                    pluralStringResource(id = R.plurals.reported_question_count, count = reports, reports)
                ),
                style = typography.regular12,
                color = colors.woodsmokeBrown
            )
        }
    }
}

private class FeedbackContent(
    @StringRes val noteRes: Int,
    @StringRes val failedTitleRes: Int,
    @StringRes val emptyTitleRes: Int,
    @StringRes val emptyBodyRes: Int,
    val skeletonTitleWidths: List<Float>,
    val skeletonSubtitleWidth: Float
)

private fun <T> LazyListScope.feedbackItems(
    feedbackList: FeedbackList<T>,
    content: FeedbackContent,
    onRetry: () -> Unit,
    key: (T) -> Any,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    when (feedbackList) {
        FeedbackList.Loading -> items(items = content.skeletonTitleWidths) { titleWidth ->
            ModerateSkeletonRow(
                modifier = Modifier.fillMaxWidth(),
                titleWidthFraction = titleWidth,
                subtitleWidthFraction = content.skeletonSubtitleWidth
            )
        }

        FeedbackList.Failed -> item {
            Column(horizontalAlignment = Alignment.Start) {
                ReleaseStatusBanner(
                    modifier = Modifier.fillMaxWidth(),
                    type = ReleaseBannerType.ERROR,
                    title = stringResource(id = content.failedTitleRes),
                    message = stringResource(id = R.string.check_connection)
                )
                TextButton(onClick = onRetry) {
                    Text(
                        text = stringResource(id = R.string.try_again),
                        style = typography.bold14,
                        color = colors.maroon
                    )
                }
            }
        }

        is FeedbackList.Loaded if feedbackList.items.isEmpty() -> item {
            ModerateClearState(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .fillMaxWidth(),
                titleRes = content.emptyTitleRes,
                bodyRes = content.emptyBodyRes
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
            ),
            reportedQuestions = FeedbackList.Loaded(items = listOf(RankedReportedQuestion.dummy())),
            libraryTitleKeys = setOf("el filibusterismo")
        ),
        ModerateState(),
        ModerateState(suggestedBooks = FeedbackList.Loaded(items = emptyList())),
        ModerateState(
            selectedFeedback = ModerateContentFeedback.REPORTED_QUESTIONS,
            reportedQuestions = FeedbackList.Loaded(
                items = listOf(RankedReportedQuestion.dummy(), RankedReportedQuestion.dummy(reportCount = 8))
            )
        ),
        ModerateState(selectedFeedback = ModerateContentFeedback.REPORTED_QUESTIONS),
        ModerateState(suggestedBooks = FeedbackList.Failed)
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
