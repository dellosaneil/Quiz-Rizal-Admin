package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditQuestionDeleteSheet(
    state: EditQuestionState,
    callback: EditQuestionCallback
) {
    val original = state.original ?: return
    val chapter = state.chapter ?: return
    val remaining = chapter.questions.size.dec()
    val isDeleting by rememberUpdatedState(newValue = state.isDeleting)
    ModalBottomSheet(
        onDismissRequest = {
            if (!state.isDeleting) callback.handleAction(action = EditQuestionActions.CancelDelete)
        },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            // Keep the sheet up while the delete is in flight.
            confirmValueChange = { !isDeleting }
        ),
        containerColor = colors.ivoryMist,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.parchment) }
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(space = 14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.delete_question_title),
                style = typography.bold23,
                color = colors.espresso
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colors.warmLinen, shape = RoundedCornerShape(size = 12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.quoted, original.question),
                    style = typography.semiBold14,
                    color = colors.espresso
                )
                Text(
                    text = stringResource(
                        id = R.string.dot_separated,
                        stringResource(id = R.string.book_chapter, state.quiz?.title.orEmpty(), chapter.chapterNumber),
                        pluralStringResource(id = R.plurals.questions_left, count = remaining, remaining)
                    ),
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
            Text(
                text = state.report?.let { report ->
                    pluralStringResource(id = R.plurals.delete_question_body_with_reports, count = report.reportCount, report.reportCount)
                } ?: stringResource(id = R.string.delete_question_body),
                style = typography.regular13.copy(fontSize = 14.sp, lineHeight = 21.sp),
                color = colors.woodsmokeBrown
            )
            if (state.deleteFailed) {
                Text(
                    text = stringResource(id = R.string.delete_question_failed),
                    style = typography.semiBold13,
                    color = colors.brickRed
                )
            }
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    onClick = { callback.handleAction(action = EditQuestionActions.CancelDelete) },
                    enabled = !state.isDeleting,
                    shape = RoundedCornerShape(size = 12.dp),
                    border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR)
                ) {
                    Text(text = stringResource(id = R.string.keep_it), style = typography.bold14, color = colors.espresso)
                }
                Button(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    onClick = { callback.handleAction(action = EditQuestionActions.ConfirmDelete) },
                    enabled = !state.isDeleting,
                    shape = RoundedCornerShape(size = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.brickRed,
                        contentColor = colors.white,
                        disabledContainerColor = colors.brickRed.copy(alpha = 0.7f),
                        disabledContentColor = colors.white
                    )
                ) {
                    if (state.isDeleting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(size = 16.dp),
                            color = colors.white,
                            trackColor = colors.white.copy(alpha = 0.35f),
                            strokeWidth = 2.dp
                        )
                    }
                    Text(text = stringResource(id = R.string.delete), style = typography.bold14)
                }
            }
        }
    }
}
