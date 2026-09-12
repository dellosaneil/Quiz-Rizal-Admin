package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
    callback: AddQuestionCallback
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(id = R.string.choices_tap_to_mark_correct_answer),
            style = typography.semiBold10,
            color = colors.taupe
        )
        for (i in 0 until 4) {
            Choice(
                modifier = Modifier.fillMaxWidth(),
                isSelected = correctAnswerIndex == i
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
    onClick: () -> Unit
) {
    val state = rememberTextFieldState()
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CommonRadioButton(isSelected = isSelected, onClick = onClick)
        CommonTextField(
            state = state,
            modifier = Modifier.height(height = 62.dp),
            textAlign = TextAlign.Left,
            textStyle = typography.regular12,
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
            callback = AddQuestionCallback.default()
        )
    }
}
