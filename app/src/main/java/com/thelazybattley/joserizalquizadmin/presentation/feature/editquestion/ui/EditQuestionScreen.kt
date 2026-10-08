package com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.domain.model.reportedquestions.RankedReportedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui.AddQuestionChapterCard
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui.CHOICE_LETTERS
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui.QuestionChoices
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui.QuestionTextField
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.editquestion.EditQuestionViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun EditQuestionScreen(
    modifier: Modifier = Modifier,
    navigate: (EditQuestionDestinations) -> Unit
) {
    val viewModel = hiltViewModel<EditQuestionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            viewModel.handleAction(action = EditQuestionActions.Navigate(destination = null))
        }
    }
    EditQuestionScreen(modifier = modifier, state = state, callback = viewModel)
}

@Composable
private fun EditQuestionScreen(
    modifier: Modifier = Modifier,
    state: EditQuestionState,
    callback: EditQuestionCallback
) {
    // Leaving mid-save would hide whether the change was stored.
    BackHandler(enabled = state.isBusy) {}

    if (state.showDeleteSheet) {
        EditQuestionDeleteSheet(state = state, callback = callback)
    }

    val goBack = {
        callback.handleAction(action = EditQuestionActions.Navigate(destination = EditQuestionDestinations.Back))
    }
    Scaffold(
        modifier = modifier,
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {
                IconButton(
                    onClick = goBack,
                    enabled = !state.isBusy,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = colors.espresso,
                        disabledContentColor = colors.taupe
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
                Text(
                    modifier = Modifier
                        .weight(weight = 1f)
                        .semantics { heading() },
                    text = stringResource(id = R.string.edit_question),
                    style = typography.bold16.copy(fontSize = 18.sp),
                    color = colors.espresso
                )
                if (state.hasChanges && !state.isBusy) {
                    TextButton(onClick = { callback.handleAction(action = EditQuestionActions.Revert) }) {
                        Text(
                            text = stringResource(id = R.string.undo_changes),
                            style = typography.bold12.copy(fontSize = 13.sp),
                            color = colors.maroon
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        val quiz = state.quiz
        val chapter = state.chapter
        val original = state.original
        if (state.isNotFound) {
            QuestionNotFound(modifier = Modifier.padding(paddingValues = innerPadding), onBack = goBack)
            return@Scaffold
        }
        if (quiz == null || chapter == null || original == null) return@Scaffold

        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .weight(weight = 1f)
                    .verticalScroll(state = rememberScrollState())
                    .padding(top = 4.dp, bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(space = 20.dp)
            ) {
                state.report?.let { report ->
                    EditQuestionReportBanner(modifier = Modifier.fillMaxWidth(), report = report)
                }
                AddQuestionChapterCard(
                    modifier = Modifier.fillMaxWidth(),
                    bookTitle = quiz.title,
                    chapter = chapter,
                    detail = stringResource(
                        id = R.string.question_position,
                        quiz.title,
                        chapter.chapterNumber,
                        state.questionIndex + 1,
                        chapter.questions.size
                    )
                )
                // A new key after Undo changes reloads the original text into the fields.
                key(state.formVersion) {
                    Column(
                        modifier = Modifier.alpha(alpha = if (state.isBusy) 0.6f else 1f),
                        verticalArrangement = Arrangement.spacedBy(space = 20.dp)
                    ) {
                        QuestionTextField(
                            modifier = Modifier.fillMaxWidth(),
                            initialText = state.question,
                            enabled = !state.isBusy,
                            isEdited = state.isQuestionEdited,
                            onTextChange = { text ->
                                callback.handleAction(action = EditQuestionActions.UpdateQuestion(question = text))
                            }
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
                            QuestionChoices(
                                modifier = Modifier.fillMaxWidth(),
                                initialChoices = state.choices,
                                correctAnswerIndex = state.correctAnswerIndex,
                                duplicateIndices = state.duplicateChoiceIndices,
                                editedIndices = state.editedChoiceIndices,
                                enabled = !state.isBusy,
                                onChoiceChange = { index, text ->
                                    callback.handleAction(action = EditQuestionActions.UpdateChoice(index = index, choice = text))
                                },
                                onSelect = { index ->
                                    callback.handleAction(action = EditQuestionActions.SelectAnswer(index = index))
                                }
                            )
                            if (state.isAnswerMoved && state.originalAnswerIndex >= 0 && state.correctAnswerIndex >= 0) {
                                AnswerMovedNote(
                                    from = CHOICE_LETTERS.getOrElse(state.originalAnswerIndex) { "" },
                                    to = CHOICE_LETTERS.getOrElse(state.correctAnswerIndex) { "" }
                                )
                            }
                        }
                    }
                }
                TextButton(
                    onClick = { callback.handleAction(action = EditQuestionActions.RequestDelete) },
                    enabled = !state.isBusy
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = colors.brickRed,
                        modifier = Modifier.size(size = 18.dp)
                    )
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(id = R.string.delete_this_question),
                        style = typography.bold14,
                        color = colors.brickRed
                    )
                }
            }
            EditQuestionSaveBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                state = state,
                callback = callback
            )
        }
    }
}

@Composable
private fun AnswerMovedNote(from: String, to: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(size = 6.dp)
                .background(color = colors.antiqueGold, shape = CircleShape)
        )
        Text(
            text = stringResource(id = R.string.answer_changed_from_to, from, to),
            style = typography.regular12,
            color = colors.antiqueGold
        )
    }
}

@Composable
private fun QuestionNotFound(modifier: Modifier = Modifier, onBack: () -> Unit) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.question_not_found_title),
            style = typography.semiBold18,
            color = colors.espresso
        )
        Text(
            text = stringResource(id = R.string.question_not_found_body),
            style = typography.regular13,
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
        TextButton(onClick = onBack) {
            Text(text = stringResource(id = R.string.back), style = typography.bold14, color = colors.maroon)
        }
    }
}

private class EditQuestionStateProvider : PreviewParameterProvider<EditQuestionState> {
    private val question = Question(
        question = "What does Ibarra vow to do after learning of his father's fate?",
        choices = listOf("Take revenge on Padre Dámaso", "Build a school", "Leave the Philippines", "Join the friars"),
        answer = "Take revenge on Padre Dámaso"
    )
    private val quiz = Quiz.dummy().let { quiz ->
        quiz.copy(chapters = listOf(quiz.chapters.first().copy(questions = listOf(question))))
    }
    private val base = EditQuestionState(
        chapterNumber = 1,
        quiz = quiz,
        questionIndex = 0,
        original = question,
        question = question.question,
        choices = question.choices,
        correctAnswerIndex = 0
    )
    override val values = sequenceOf(
        base,
        base.copy(
            report = RankedReportedQuestion.dummy(),
            choices = question.choices.toMutableList().apply { this[1] = "Build a school in San Diego" },
            correctAnswerIndex = 1
        ),
        EditQuestionState(isNotFound = true)
    )
}

@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(EditQuestionStateProvider::class) state: EditQuestionState) {
    AppTheme {
        EditQuestionScreen(modifier = Modifier.fillMaxSize(), state = state, callback = EditQuestionCallback.default())
    }
}
