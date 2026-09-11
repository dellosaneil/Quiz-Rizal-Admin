package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentTabScreen(
            modifier = Modifier.fillMaxSize(),
            state = ContentState(),
            callback = ContentCallback.default()
        )
    }
}
