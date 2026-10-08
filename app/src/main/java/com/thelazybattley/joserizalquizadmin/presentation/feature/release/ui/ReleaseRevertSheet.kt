package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChange
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReleaseRevertSheet(
    change: ReleaseChange,
    isReverting: Boolean,
    revertFailed: Boolean,
    callback: ReleaseCallback
) {
    val (question, location, bodyRes) = when (change) {
        is ReleaseChange.QuestionAdded -> Triple(change.question, change.bookTitle to change.chapterNumber, R.string.revert_new_body)
        is ReleaseChange.QuestionEdited -> Triple(change.question, change.bookTitle to change.chapterNumber, R.string.revert_edited_body)
        is ReleaseChange.QuestionRemoved -> Triple(change.question, change.bookTitle to change.chapterNumber, R.string.revert_removed_body)
        else -> return
    }
    val reverting by rememberUpdatedState(newValue = isReverting)
    ModalBottomSheet(
        onDismissRequest = { if (!isReverting) callback.handleAction(action = ReleaseActions.CancelRevert) },
        sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true,
            // Keep the sheet up while the revert is in flight.
            confirmValueChange = { !reverting }
        ),
        containerColor = colors.ivoryMist,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.parchment) }
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(space = 14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.revert_change_title),
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
                    text = stringResource(id = R.string.quoted, question),
                    style = typography.semiBold14,
                    color = colors.espresso
                )
                Text(
                    text = stringResource(id = R.string.book_chapter, location.first, location.second),
                    style = typography.regular12,
                    color = colors.woodsmokeBrown
                )
            }
            Text(
                text = stringResource(id = bodyRes),
                style = typography.regular13.copy(fontSize = 14.sp, lineHeight = 21.sp),
                color = colors.woodsmokeBrown
            )
            if (revertFailed) {
                Text(
                    text = stringResource(id = R.string.revert_failed),
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
                    onClick = { callback.handleAction(action = ReleaseActions.CancelRevert) },
                    enabled = !isReverting,
                    shape = RoundedCornerShape(size = 12.dp),
                    border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR)
                ) {
                    Text(text = stringResource(id = R.string.keep_it), style = typography.bold14, color = colors.espresso)
                }
                Button(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    onClick = { callback.handleAction(action = ReleaseActions.ConfirmRevert) },
                    enabled = !isReverting,
                    shape = RoundedCornerShape(size = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colors.maroon,
                        contentColor = colors.white,
                        disabledContainerColor = colors.maroon.copy(alpha = 0.7f),
                        disabledContentColor = colors.white
                    )
                ) {
                    if (isReverting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(size = 16.dp),
                            color = colors.white,
                            trackColor = colors.white.copy(alpha = 0.35f),
                            strokeWidth = 2.dp
                        )
                    }
                    Text(text = stringResource(id = R.string.revert), style = typography.bold14)
                }
            }
        }
    }
}
