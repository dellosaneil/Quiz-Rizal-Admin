package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonSegmentedControl
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme

@Composable
fun ModerateSegmentedControl(
    modifier: Modifier = Modifier,
    selectedFeedback: ModerateContentFeedback,
    callbacks: ModerateCallback
) {
    CommonSegmentedControl(
        modifier = modifier,
        selectedOption = selectedFeedback,
        callback = {

        },
        choices = ModerateContentFeedback.entries,
        stringResources = ModerateContentFeedback.entries.map { it.id }
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ModerateSegmentedControl(
            selectedFeedback = ModerateContentFeedback.SUGGESTED_BOOKS,
            callbacks = ModerateCallback.default()
        )
    }
}
