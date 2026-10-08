package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.getTotalQuestions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateSkeletonRow
import com.thelazybattley.joserizalquizadmin.presentation.util.Category
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentState
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun ContentTabScreen(
    modifier: Modifier = Modifier,
    navigate: (ContentDestinations) -> Unit = {}
) {
    val viewModel = hiltViewModel<ContentViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            viewModel.handleAction(action = ContentActions.Navigate(destination = null))
        }
    }
    ContentTabScreen(
        modifier = modifier,
        state = state,
        callback = viewModel
    )
}

@Composable
private fun ContentTabScreen(
    modifier: Modifier = Modifier,
    state: ContentState,
    callback: ContentCallback
) {
    val addBook = {
        callback.handleAction(action = ContentActions.Navigate(destination = ContentDestinations.AddBook()))
    }
    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding() + 16.dp
            ),
            verticalArrangement = Arrangement.spacedBy(space = 10.dp)
        ) {
            item {
                ContentHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    bookCount = if (state.isLoading) null else state.quiz.size,
                    questionCount = state.quiz.sumOf { it.chapters.getTotalQuestions() },
                    onAddBook = addBook
                )
            }
            when {
                state.isLoading -> items(items = listOf(0.62f, 0.74f, 0.5f, 0.68f)) { titleWidth ->
                    ModerateSkeletonRow(
                        modifier = Modifier.fillMaxWidth(),
                        titleWidthFraction = titleWidth,
                        subtitleWidthFraction = 0.34f
                    )
                }

                state.quiz.isEmpty() -> item {
                    ContentEmptyState(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth(),
                        onAddBook = addBook
                    )
                }

                else -> {
                    item {
                        ContentFilterChips(
                            modifier = Modifier.fillMaxWidth(),
                            quiz = state.quiz,
                            selected = state.categoryFilter,
                            callback = callback
                        )
                    }
                    val emptyCategory = state.categoryFilter?.takeIf { state.visibleQuiz.isEmpty() }
                    if (emptyCategory != null) {
                        item {
                            ContentFilterEmptyState(
                                modifier = Modifier
                                    .padding(top = 6.dp)
                                    .fillMaxWidth(),
                                category = emptyCategory,
                                onAddBook = {
                                    callback.handleAction(
                                        action = ContentActions.Navigate(
                                            destination = ContentDestinations.AddBook(category = emptyCategory)
                                        )
                                    )
                                },
                                onShowAll = {
                                    callback.handleAction(action = ContentActions.FilterSelected(category = null))
                                }
                            )
                        }
                    }
                    items(items = state.visibleQuiz, key = { it.id }) { quiz ->
                        ContentItemCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .animateItem(),
                            quiz = quiz,
                            isExpanded = quiz.id == state.expandedBook,
                            callback = callback
                        )
                    }
                }
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(ContentStateProvider::class) state: ContentState) {
    AppTheme {
        ContentTabScreen(
            modifier = Modifier.fillMaxSize(),
            state = state,
            callback = ContentCallback.default()
        )
    }
}

private class ContentStateProvider : PreviewParameterProvider<ContentState> {
    override val values = sequenceOf(
        ContentState(
            isLoading = false,
            quiz = listOf(Quiz.dummy(), Quiz.dummy(id = "1").copy(category = Category.NOVEL)),
            expandedBook = "1"
        ),
        ContentState(
            isLoading = false,
            quiz = listOf(Quiz.dummy(id = "1").copy(category = Category.NOVEL)),
            categoryFilter = Category.LIFE_OF_RIZAL
        ),
        ContentState(),
        ContentState(isLoading = false)
    )
}
