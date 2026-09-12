package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.layout.Arrangement
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
    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            stickyHeader {
                ContentStickyHeader(
                    callback = callback,
                    count = state.quiz.size
                )
            }
            items(
                items = state.quiz, key = { it.id }
            ) { quiz ->
                ContentItemCard(
                    quiz = quiz,
                    modifier = Modifier.fillMaxWidth(),
                    isExpanded = quiz.id == state.expandedBook,
                    callback = callback
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentTabScreen(
            modifier = Modifier.fillMaxSize(),
            state = ContentState(
                quiz = listOf(
                    Quiz.dummy(),
                    Quiz.dummy(id = "1"),
                )
            ),
            callback = ContentCallback.default()
        )
    }
}
