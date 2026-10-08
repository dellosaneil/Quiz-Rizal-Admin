package com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.suggestedbooks.RankedSuggestedBook
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ModerateCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModerateRemoveSuggestionSheet(
    suggestedBook: RankedSuggestedBook,
    callbacks: ModerateCallback
) {
    ModalBottomSheet(
        onDismissRequest = { callbacks.handleAction(action = ModerateActions.CancelRemoveSuggestion) },
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = colors.ivoryMist,
        dragHandle = { BottomSheetDefaults.DragHandle(color = colors.parchment) }
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(space = 14.dp)
        ) {
            Text(
                text = stringResource(id = R.string.remove_suggestion_title, suggestedBook.bookTitle),
                style = typography.bold23,
                color = colors.espresso
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = colors.warmLinen, shape = RoundedCornerShape(size = 12.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = suggestedBook.requestCount.toString(),
                    style = typography.bold23.copy(fontSize = 24.sp),
                    color = colors.espresso
                )
                Text(
                    text = pluralStringResource(
                        id = R.plurals.remove_suggestion_count,
                        count = suggestedBook.requestCount,
                        suggestedBook.requestCount
                    ),
                    style = typography.regular13,
                    color = colors.woodsmokeBrown
                )
            }
            Text(
                text = stringResource(id = R.string.remove_suggestion_body),
                style = typography.regular13.copy(fontSize = 14.sp, lineHeight = 21.sp),
                color = colors.woodsmokeBrown
            )
            Row(
                modifier = Modifier.padding(top = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                CommonButton(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    text = stringResource(id = R.string.keep_it),
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = colors.ivoryMist,
                        contentColor = colors.espresso
                    ),
                    border = BorderStroke(width = 1.dp, color = APP_BORDER_COLOR)
                ) {
                    callbacks.handleAction(action = ModerateActions.CancelRemoveSuggestion)
                }
                CommonButton(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .height(height = 48.dp),
                    text = stringResource(id = R.string.remove),
                    buttonColors = ButtonDefaults.buttonColors(
                        containerColor = colors.brickRed,
                        contentColor = colors.white
                    )
                ) {
                    callbacks.handleAction(action = ModerateActions.ConfirmRemoveSuggestion)
                }
            }
        }
    }
}
