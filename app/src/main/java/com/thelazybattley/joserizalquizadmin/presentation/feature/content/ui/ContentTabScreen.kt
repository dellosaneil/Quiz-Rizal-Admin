package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentState
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme

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

}


@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentTabScreen()
    }
}
