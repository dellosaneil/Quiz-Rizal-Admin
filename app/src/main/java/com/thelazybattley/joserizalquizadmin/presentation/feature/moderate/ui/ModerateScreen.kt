package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateState
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun ModerateScreen(
    modifier: Modifier = Modifier,
    state: ModerateState,
    callbacks: ModerateCallback
) {
    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {
            item {
                ModerateSegmentedControl(
                    selectedFeedback = ModerateContentFeedback.SUGGESTED_BOOKS,
                    callbacks = callbacks
                )
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    ModerateScreen(
        modifier = Modifier,
        state = ModerateState(),
        callbacks = ModerateCallback.default()
    )
}
