package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldDecorator
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.release.ui.ReleaseTag
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR

val CHOICE_LETTERS = listOf("A", "B", "C", "D", "E", "F")

// Shared by Add Question and Edit Question. Wrap in key() to reset the fields to initialChoices.
@Composable
fun QuestionChoices(
    modifier: Modifier = Modifier,
    initialChoices: List<String> = List(size = AddQuestionState.CHOICE_COUNT) { "" },
    correctAnswerIndex: Int,
    duplicateIndices: Set<Int>,
    editedIndices: Set<Int> = emptySet(),
    enabled: Boolean = true,
    onChoiceChange: (index: Int, text: String) -> Unit,
    onSelect: (index: Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(space = 2.dp)) {
            Text(
                text = stringResource(id = R.string.choices),
                style = typography.semiBold11.copy(fontFamily = FontFamily.Monospace),
                color = colors.taupe
            )
            Text(
                text = stringResource(id = R.string.choices_hint),
                style = typography.regular12,
                color = colors.woodsmokeBrown
            )
        }
        Column(
            modifier = Modifier.selectableGroup(),
            verticalArrangement = Arrangement.spacedBy(space = 8.dp)
        ) {
            repeat(times = initialChoices.size) { index ->
                ChoiceRow(
                    modifier = Modifier.fillMaxWidth(),
                    index = index,
                    initialText = initialChoices.getOrElse(index) { "" },
                    isCorrect = correctAnswerIndex == index,
                    isDuplicate = index in duplicateIndices,
                    isEdited = index in editedIndices,
                    isLast = index == initialChoices.lastIndex,
                    enabled = enabled,
                    onTextChange = { text -> onChoiceChange(index, text) },
                    onSelect = { onSelect(index) }
                )
            }
        }
    }
}

@Composable
private fun ChoiceRow(
    modifier: Modifier = Modifier,
    index: Int,
    initialText: String,
    isCorrect: Boolean,
    isDuplicate: Boolean,
    isEdited: Boolean,
    isLast: Boolean,
    enabled: Boolean,
    onTextChange: (String) -> Unit,
    onSelect: () -> Unit
) {
    val state = rememberTextFieldState(initialText = initialText)
    val focusManager = LocalFocusManager.current
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { state.text }.collect { text ->
            onTextChange(text.toString())
        }
    }
    val letter = CHOICE_LETTERS.getOrElse(index) { (index + 1).toString() }
    val shape = RoundedCornerShape(size = 14.dp)
    val (containerColor, borderColor, borderWidth) = when {
        isCorrect -> Triple(colors.softSage, colors.deepMoss, 2.dp)
        isDuplicate -> Triple(colors.ivoryMist, colors.brickRed, 1.5.dp)
        else -> Triple(colors.ivoryMist, APP_BORDER_COLOR, 1.dp)
    }
    val moveFocus = {
        if (isLast) focusManager.clearFocus() else focusManager.moveFocus(focusDirection = FocusDirection.Next)
    }
    val markDescription = stringResource(id = R.string.mark_choice_correct, letter)
    val fieldDescription = stringResource(id = R.string.choice_letter, letter)
    Row(
        modifier = modifier
            .background(color = containerColor, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
            .padding(all = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size = 44.dp)
                .clip(shape = CircleShape)
                // Must come before selectable, which adds the focus target it applies to.
                // Keeps Tab and Next moving from one choice field to the next.
                .focusProperties { canFocus = false }
                .selectable(
                    selected = isCorrect,
                    enabled = enabled,
                    role = Role.RadioButton,
                    onClick = onSelect
                )
                .semantics { contentDescription = markDescription },
            contentAlignment = Alignment.Center
        ) {
            if (isCorrect) {
                Box(
                    modifier = Modifier
                        .size(size = 26.dp)
                        .background(color = colors.deepMoss, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = colors.softSage,
                        modifier = Modifier.size(size = 16.dp)
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .size(size = 26.dp)
                        .background(color = colors.ivoryMist, shape = CircleShape)
                        .border(width = 2.dp, color = colors.taupe, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = letter,
                        style = typography.semiBold12.copy(fontFamily = FontFamily.Monospace),
                        color = colors.woodsmokeBrown
                    )
                }
            }
        }
        BasicTextField(
            modifier = Modifier
                .weight(weight = 1f)
                .height(height = 44.dp)
                .semantics { contentDescription = fieldDescription }
                .onPreviewKeyEvent {
                    if (it.key == Key.Tab && it.type == KeyEventType.KeyDown) {
                        moveFocus()
                        true
                    } else {
                        false
                    }
                },
            state = state,
            enabled = enabled,
            textStyle = typography.regular13.copy(fontSize = 15.sp, color = colors.espresso),
            lineLimits = TextFieldLineLimits.SingleLine,
            cursorBrush = SolidColor(value = colors.maroon),
            keyboardOptions = KeyboardOptions(imeAction = if (isLast) ImeAction.Done else ImeAction.Next),
            onKeyboardAction = { moveFocus() },
            decorator = TextFieldDecorator { innerTextField ->
                Box(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (state.text.isEmpty()) {
                        Text(
                            text = fieldDescription,
                            style = typography.regular13.copy(fontSize = 15.sp),
                            color = colors.taupe
                        )
                    }
                    innerTextField()
                }
            }
        )
        when {
            isCorrect -> ChoiceTag(
                text = stringResource(id = R.string.answer_tag),
                containerColor = colors.ivoryMist,
                contentColor = colors.deepMoss
            )

            isDuplicate -> ChoiceTag(
                text = stringResource(id = R.string.same_tag),
                containerColor = colors.softBlush,
                contentColor = colors.brickRed
            )

            isEdited -> EditedTag(modifier = Modifier.padding(end = 10.dp))
        }
    }
}

@Composable
private fun ChoiceTag(text: String, containerColor: Color, contentColor: Color) {
    ReleaseTag(
        modifier = Modifier.padding(end = 10.dp),
        text = text,
        containerColor = containerColor,
        contentColor = contentColor
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        QuestionChoices(
            modifier = Modifier
                .background(color = colors.warmLinen)
                .fillMaxWidth(),
            correctAnswerIndex = 0,
            duplicateIndices = setOf(2, 3),
            editedIndices = setOf(1),
            onChoiceChange = { _, _ -> },
            onSelect = {}
        )
    }
}
