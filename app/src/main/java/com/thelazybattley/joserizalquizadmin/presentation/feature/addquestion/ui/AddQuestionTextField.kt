package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionAction
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

@Composable
fun AddQuestionTextField(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    callback: AddQuestionCallback
) {
    val state = rememberTextFieldState()
    val focusManager = LocalFocusManager.current
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { state.text }.collect { text ->
            callback.handleAction(action = AddQuestionAction.UpdateQuestion(question = text.toString()))
        }
    }
    val shape = RoundedCornerShape(size = 12.dp)
    val label = stringResource(id = R.string.question)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = typography.semiBold11.copy(fontFamily = FontFamily.Monospace),
                color = colors.taupe
            )
            if (state.text.isNotEmpty()) {
                Text(
                    text = pluralStringResource(id = R.plurals.character_count, count = state.text.length, state.text.length),
                    style = typography.regular11,
                    color = colors.taupe
                )
            }
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 104.dp)
                .background(color = colors.ivoryMist, shape = shape)
                .border(
                    width = if (isFocused) 2.dp else 1.dp,
                    color = if (isFocused) colors.antiqueGold else APP_BORDER_COLOR,
                    shape = shape
                )
                .semantics { contentDescription = label }
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        true
                    } else {
                        false
                    }
                },
            state = state,
            enabled = enabled,
            textStyle = typography.regular13.copy(fontSize = 15.sp, lineHeight = 22.sp, color = colors.espresso),
            lineLimits = TextFieldLineLimits.MultiLine(),
            cursorBrush = SolidColor(value = colors.maroon),
            interactionSource = interactionSource,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            decorator = TextFieldDecorator { innerTextField ->
                Box(modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.question_placeholder),
                            style = typography.regular13.copy(fontSize = 15.sp, lineHeight = 22.sp),
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
        AddQuestionTextField(
            modifier = Modifier.fillMaxWidth(),
            callback = AddQuestionCallback.default()
        )
    }
}
