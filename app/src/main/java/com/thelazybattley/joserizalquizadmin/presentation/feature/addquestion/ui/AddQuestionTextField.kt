package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
            modifier = Modifier.height(height = 128.dp),
            textAlign = TextAlign.Left,
            contentAlignment = Alignment.TopStart,
            lineLimits = TextFieldLineLimits.Default
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
