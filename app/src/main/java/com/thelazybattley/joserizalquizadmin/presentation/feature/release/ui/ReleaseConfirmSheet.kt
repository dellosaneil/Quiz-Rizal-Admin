package com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChange
import com.thelazybattley.joserizalquizadmin.domain.model.release.ReleaseChangeKind
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ReleaseCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReleaseConfirmSheet(
    changes: List<ReleaseChange>,
    callback: ReleaseCallback
) {
    val countByKind = changes.groupingBy { it.kind }.eachCount()
    ModalBottomSheet(
        onDismissRequest = { callback.handleAction(action = ReleaseActions.CancelPush) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colors.ivoryMist,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.parchment) }
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(space = 14.dp)
        ) {
            Text(
                text = pluralStringResource(id = R.plurals.push_changes_question, count = changes.size, changes.size),
                style = typography.bold23,
                color = colors.espresso
            )
            Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                ReleaseChangeKind.entries.forEach { kind ->
                    KindCount(
                        modifier = Modifier.weight(weight = 1f),
                        count = countByKind[kind] ?: 0,
                        label = stringResource(
                            id = when (kind) {
                                ReleaseChangeKind.NEW -> R.string.count_new
                                ReleaseChangeKind.EDITED -> R.string.count_edited
                                ReleaseChangeKind.REMOVED -> R.string.count_removed
                            }
                        )
                    )
                }
            }
            Text(
                text = stringResource(id = R.string.push_confirm_body),
                style = typography.regular13,
                color = colors.woodsmokeBrown
            )
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                CommonButton(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    text = stringResource(id = R.string.cancel),
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = colors.ivoryMist,
                        contentColor = colors.espresso
                    ),
                    border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR)
                ) {
                    callback.handleAction(action = ReleaseActions.CancelPush)
                }
                CommonButton(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    text = stringResource(id = R.string.push_now)
                ) {
                    callback.handleAction(action = ReleaseActions.ConfirmPush)
                }
            }
        }
    }
}

@Composable
private fun KindCount(modifier: Modifier = Modifier, count: Int, label: String) {
    Column(
        modifier = modifier
            .background(color = colors.warmLinen, shape = RoundedCornerShape(size = 12.dp))
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        Text(text = count.toString(), style = typography.bold23.copy(fontSize = 20.sp), color = colors.espresso)
        Text(text = label, style = typography.regular11, color = colors.woodsmokeBrown)
    }
}
