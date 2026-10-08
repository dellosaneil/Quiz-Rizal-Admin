package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditBlocker
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseBannerType
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseStatusBanner
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun EditQuestionSaveBar(
    modifier: Modifier = Modifier,
    state: EditQuestionState,
    callback: EditQuestionCallback
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        if (state.saveFailed) {
            ReleaseStatusBanner(
                modifier = Modifier.fillMaxWidth(),
                type = ReleaseBannerType.ERROR,
                title = stringResource(id = R.string.save_changes_failed_title),
                message = stringResource(id = R.string.save_question_failed_message)
            )
        }
        state.report?.let { report ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .toggleable(
                        value = state.clearReports,
                        enabled = !state.isBusy,
                        role = Role.Checkbox,
                        onValueChange = { callback.handleAction(action = EditQuestionActions.ToggleClearReports) }
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.clearReports,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(checkedColor = colors.maroon, uncheckedColor = colors.taupe)
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = pluralStringResource(id = R.plurals.also_clear_reports, count = report.reportCount, report.reportCount),
                    style = typography.regular13,
                    color = colors.espresso
                )
            }
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = when {
                state.isSaving -> stringResource(id = R.string.saving_question)
                else -> when (state.blocker) {
                    EditBlocker.MISSING_QUESTION -> stringResource(id = R.string.save_hint_question)
                    EditBlocker.BLANK_CHOICES -> pluralStringResource(
                        id = R.plurals.save_hint_blank_choices,
                        count = state.blankChoiceCount,
                        state.blankChoiceCount
                    )

                    EditBlocker.DUPLICATE_CHOICES -> stringResource(id = R.string.save_hint_duplicate)
                    EditBlocker.NO_ANSWER -> stringResource(id = R.string.save_hint_answer)
                    EditBlocker.NO_CHANGES -> stringResource(id = R.string.edit_hint_no_changes)
                    null -> stringResource(id = R.string.edit_hint_ready)
                }
            },
            style = typography.regular12,
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 52.dp),
            onClick = { callback.handleAction(action = EditQuestionActions.Save) },
            enabled = state.blocker == null && !state.isBusy,
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
                text = stringResource(id = if (state.isSaving) R.string.saving else R.string.save_changes),
                style = typography.bold14
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        EditQuestionSaveBar(
            state = EditQuestionState(report = RankedReportedQuestion.dummy()),
            callback = EditQuestionCallback.default()
        )
    }
}
