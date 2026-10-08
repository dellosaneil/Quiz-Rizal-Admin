package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookTextFieldTypes
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun AddBookTextField(
    modifier: Modifier = Modifier,
    type: AddBookTextFieldTypes,
    initialText: String = "",
    enabled: Boolean = true,
    callback: AddBookCallback
) {
    val state = rememberTextFieldState(initialText = initialText)
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { state.text }
            .collect { text ->
                callback.handleAction(
                    action = AddBookActions.TextFieldUpdated(
                        text = text.toString(),
                        type = type
                    )
                )
            }
    }
    val shape = RoundedCornerShape(size = 12.dp)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
    ) {
        Text(
            text = stringResource(id = type.id),
            style = typography.semiBold13,
            color = colors.espresso
        )
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = 50.dp)
                .background(color = colors.ivoryMist, shape = shape)
                .border(
                    width = if (isFocused) 2.dp else 1.dp,
                    color = if (isFocused) colors.antiqueGold else APP_BORDER_COLOR,
                    shape = shape
                ),
            state = state,
            enabled = enabled,
            textStyle = typography.regular13.copy(fontSize = 15.sp, color = colors.espresso),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(value = colors.maroon),
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            decorator = TextFieldDecorator { innerTextField ->
                Box(
                    modifier = Modifier.padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = stringResource(id = type.placeholderId),
                            style = typography.regular13.copy(fontSize = 15.sp),
                            color = colors.taupe
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookTextField(
            modifier = Modifier.fillMaxWidth(),
            type = AddBookTextFieldTypes.TITLE,
            callback = AddBookCallback.default()
        )
    }
}
