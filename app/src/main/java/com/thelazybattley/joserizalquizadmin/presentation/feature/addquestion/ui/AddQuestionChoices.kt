package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionAction
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonRadioButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTextField
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun AddQuestionChoices(
    modifier: Modifier = Modifier,
    correctAnswerIndex: Int,
    callback: AddQuestionCallback,
    showSuccessBanner: Boolean
) {
    val focusManager = LocalFocusManager.current
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.choices_tap_to_mark_correct_answer),
            style = typography.semiBold10,
            color = colors.taupe
        )
        for (i in 0 until 4) {
            val state = rememberTextFieldState()
            LaunchedEffect(key1 = showSuccessBanner) {
                if (showSuccessBanner) {
                    state.clearText()
                }
            }
            LaunchedEffect(key1 = state.text) {
                callback.handleAction(
                    action = AddQuestionAction.Choice.UpdateValue(
                        index = i,
                        choice = state.text.toString()
                    )
                )
            }
            Choice(
                modifier = Modifier.fillMaxWidth(),
                isSelected = correctAnswerIndex == i,
                state = state,
                keyboardOptions = KeyboardOptions(
                    imeAction = if (i == 3) ImeAction.Done else ImeAction.Next
                ),
                onKeyboardAction = {
                    if (i == 3) {
                        focusManager.clearFocus()
                    } else {
                        focusManager.moveFocus(focusDirection = FocusDirection.Next)
                    }
                },
                onTabPressed = {
                    if (i == 3) {
                        focusManager.clearFocus()
                    } else {
                        focusManager.moveFocus(focusDirection = FocusDirection.Next)
                    }
                }
            ) {
                callback.handleAction(action = AddQuestionAction.Choice.Selected(index = i))
            }
        }
    }
}

@Composable
private fun Choice(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    state: TextFieldState,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    onTabPressed: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonRadioButton(
            isSelected = isSelected,
            onClick = onClick,
            modifier = Modifier.focusProperties { canFocus = false }
        )
        CommonTextField(
            state = state,
            modifier = Modifier
                .height(height = 62.dp)
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        onTabPressed()
                        true
                    } else {
                        false
                    }
                },
            textAlign = TextAlign.Left,
            textStyle = typography.regular12,
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddQuestionChoices(
            modifier = Modifier
                .background(color = APP_BACKGROUND)
                .fillMaxWidth(),
            correctAnswerIndex = -1,
            callback = AddQuestionCallback.default(),
            showSuccessBanner = false
        )
    }
}
