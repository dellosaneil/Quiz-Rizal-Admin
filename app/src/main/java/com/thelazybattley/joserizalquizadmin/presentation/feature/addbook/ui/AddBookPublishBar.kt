package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.PublishBlocker
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.PublishPhase
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseBannerType
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseStatusBanner
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun AddBookPublishBar(
    modifier: Modifier = Modifier,
    state: AddBookState,
    callback: AddBookCallback
) {
    val isPublishing = state.publishPhase == PublishPhase.PUBLISHING
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        if (state.publishFailed) {
            ReleaseStatusBanner(
                modifier = Modifier.fillMaxWidth(),
                type = ReleaseBannerType.ERROR,
                title = stringResource(id = R.string.publish_book_failed_title),
                message = stringResource(id = R.string.publish_book_failed_message)
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when {
                isPublishing -> stringResource(id = R.string.publish_hint_publishing)
                state.blocker == PublishBlocker.MISSING_DETAILS -> stringResource(id = R.string.publish_hint_missing_details)
                state.blocker == PublishBlocker.BLANK_CHAPTERS -> pluralStringResource(
                    id = R.plurals.publish_hint_blank_chapters,
                    count = state.blankChapterCount,
                    state.blankChapterCount
                )

                else -> stringResource(id = R.string.publish_hint_ready)
            },
            style = typography.regular12,
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 52.dp),
            onClick = { callback.handleAction(action = AddBookActions.PublishBook) },
            enabled = state.blocker == null && !isPublishing,
            shape = RoundedCornerShape(size = 12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.maroon,
                contentColor = colors.white,
                disabledContainerColor = if (isPublishing) colors.maroon.copy(alpha = 0.7f) else colors.parchment,
                disabledContentColor = if (isPublishing) colors.white else colors.woodsmokeBrown
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isPublishing) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(size = 16.dp),
                        color = colors.white,
                        trackColor = colors.white.copy(alpha = 0.35f),
                        strokeWidth = 2.dp
                    )
                }
                Text(
                    text = stringResource(id = if (isPublishing) R.string.publishing else R.string.publish_to_debug),
                    style = typography.bold14
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookPublishBar(
            state = AddBookState(title = "Noli Me Tangere", author = "José Rizal", chapters = listOf("Isang Pagtitipon", "")),
            callback = AddBookCallback.default()
        )
    }
}
