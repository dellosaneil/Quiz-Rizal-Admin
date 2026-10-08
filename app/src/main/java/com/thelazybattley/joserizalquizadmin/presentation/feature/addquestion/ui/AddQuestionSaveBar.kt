package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionAction
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.SaveBlocker
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseBannerType
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseStatusBanner
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun AddQuestionSaveBar(
    modifier: Modifier = Modifier,
    state: AddQuestionState,
    callback: AddQuestionCallback
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        if (state.saveFailed) {
            ReleaseStatusBanner(
                modifier = Modifier.fillMaxWidth(),
                type = ReleaseBannerType.ERROR,
                title = stringResource(id = R.string.save_question_failed_title),
                message = stringResource(id = R.string.save_question_failed_message)
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when {
                state.isSaving -> stringResource(id = R.string.saving_question)
                else -> when (state.blocker) {
                    SaveBlocker.MISSING_QUESTION -> stringResource(id = R.string.save_hint_question)
                    SaveBlocker.BLANK_CHOICES -> pluralStringResource(
                        id = R.plurals.save_hint_blank_choices,
                        count = state.blankChoiceCount,
                        state.blankChoiceCount
                    )

                    SaveBlocker.DUPLICATE_CHOICES -> stringResource(id = R.string.save_hint_duplicate)
                    SaveBlocker.NO_ANSWER -> stringResource(id = R.string.save_hint_answer)
                    null -> stringResource(id = R.string.save_hint_ready, state.chapterNumber)
                }
            },
            style = typography.regular12,
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
        Row(horizontalArrangement = Arrangement.spacedBy(space = 10.dp)) {
            // Offered once at least one question is saved, as a clear way to finish.
            if (state.savedCount > 0) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 52.dp),
                    onClick = {
                        callback.handleAction(
                            action = AddQuestionAction.Navigate(destination = AddQuestionDestinations.Back)
                        )
                    },
                    enabled = !state.isSaving,
                    shape = RoundedCornerShape(size = 12.dp),
                    border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR)
                ) {
                    Text(text = stringResource(id = R.string.done), style = typography.bold14, color = colors.espresso)
                }
            }
            Button(
                modifier = Modifier
                    .weight(weight = 2f)
                    .height(height = 52.dp),
                onClick = { callback.handleAction(action = AddQuestionAction.SaveQuestion) },
                enabled = state.blocker == null && !state.isSaving,
                shape = RoundedCornerShape(size = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.maroon,
                    contentColor = colors.white,
                    disabledContainerColor = if (state.isSaving) colors.maroon.copy(alpha = 0.7f) else colors.parchment,
                    disabledContentColor = if (state.isSaving) colors.white else colors.woodsmokeBrown
                )
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(size = 16.dp),
                        color = colors.white,
                        trackColor = colors.white.copy(alpha = 0.35f),
                        strokeWidth = 2.dp
                    )
                }
                Text(
                    text = stringResource(id = if (state.isSaving) R.string.saving else R.string.save_question),
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
        AddQuestionSaveBar(
            state = AddQuestionState(savedCount = 1),
            callback = AddQuestionCallback.default()
        )
    }
}
