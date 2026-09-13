package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
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
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTextField
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography

@Composable
fun AddQuestionTextField(
    modifier: Modifier = Modifier,
    callback: AddQuestionCallback,
    showSuccessBanner: Boolean
) {
    val state = rememberTextFieldState(initialText = "")
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = state.text) {
        callback.handleAction(action = AddQuestionAction.UpdateQuestion(question = state.text.toString()))
    }
    LaunchedEffect(key1 = showSuccessBanner) {
        if (showSuccessBanner) {
            state.clearText()
        }
    }
    Column(
        modifier = modifier,
    ) {
        Text(
            text = stringResource(id = R.string.question),
            color = colors.taupe,
            style = typography.semiBold10
        )
        CommonTextField(
            state = state,
            textStyle = typography.regular13,
            modifier = Modifier
                .height(height = 128.dp)
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        focusManager.moveFocus(focusDirection = FocusDirection.Next)
                        true
                    } else {
                        false
                    }
                },
            textAlign = TextAlign.Left,
            contentAlignment = Alignment.TopStart,
            lineLimits = TextFieldLineLimits.Default,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            onKeyboardAction = {
                focusManager.moveFocus(focusDirection = FocusDirection.Next)
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
            callback = AddQuestionCallback.default(),
            showSuccessBanner = false
        )
    }
}
