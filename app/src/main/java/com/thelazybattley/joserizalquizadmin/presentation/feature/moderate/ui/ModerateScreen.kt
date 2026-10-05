package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateState
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
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
        LazyColumn(
            contentPadding = innerPadding,
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            stickyHeader {
                ModerateSegmentedControl(
                    selectedFeedback = state.selectedFeedback,
                    callbacks = callbacks,
                )
            }
            item {
                val textRes = when (state.selectedFeedback) {
                    ModerateContentFeedback.SUGGESTED_BOOKS -> R.string.sorted_by_how_many_students_requested
                    ModerateContentFeedback.REPORTED_QUESTIONS -> R.string.sorted_by_number_of_reports
                }
                Text(
                    text = stringResource(id = textRes),
                    style = typography.regular12,
                    color = colors.taupe
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
