package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@Composable
fun AddBookChapterTextField(
    modifier: Modifier = Modifier,
    index: Int,
    callback: AddBookCallback
) {
    val state = rememberTextFieldState()

    LaunchedEffect(key1 = Unit) {
        snapshotFlow { state.text }
            .debounce(timeout = 150.milliseconds)
            .collect { text ->
                callback.handleAction(
                    action = AddBookActions.Chapter.Update(
                        index = index,
                        text = text.toString()
                    )
                )
            }
    }

    TextField(
        state = state,
        prefix = {
            Text(
                text = "${index.inc()}.",
                style = typography.semiBold11
            )
        },
        textStyle = typography.regular12,
        modifier = modifier.border(
            width = 1.dp,
            color = APP_BORDER_COLOR,
            shape = RoundedCornerShape(size = 8.dp)
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = colors.espresso,
            unfocusedTextColor = colors.espresso,
            focusedContainerColor = colors.ivoryMist,
            unfocusedContainerColor = colors.ivoryMist,
            focusedPrefixColor = colors.antiqueGold,
            unfocusedPrefixColor = colors.antiqueGold,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(size = 8.dp),
        suffix = {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                tint = colors.taupe,
                modifier = Modifier
                    .clickable {
                        callback.handleAction(
                            action = AddBookActions.Chapter.Delete(
                                index = index
                            )
                        )
                    }
                    .border(
                        width = 1.dp,
                        color = APP_BORDER_COLOR,
                        shape = RoundedCornerShape(size = 8.dp)
                    )
                    .clip(shape = RoundedCornerShape(size = 8.dp))
            )
        },
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookChapterTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 56.dp),
            index = 1,
            callback = AddBookCallback.default()
        )
    }
}
