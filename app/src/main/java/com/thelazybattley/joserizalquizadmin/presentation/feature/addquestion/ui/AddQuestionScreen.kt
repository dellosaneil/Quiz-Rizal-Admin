package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionAction
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionViewModel
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.SavedQuestion
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun AddQuestionScreen(
    modifier: Modifier = Modifier,
    navigate: (AddQuestionDestinations) -> Unit
) {
    val viewModel = hiltViewModel<AddQuestionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            viewModel.handleAction(action = AddQuestionAction.Navigate(destination = null))
        }
    }
    Screen(
        state = state,
        modifier = modifier,
        callback = viewModel
    )
}

@Composable
private fun Screen(
    modifier: Modifier = Modifier,
    state: AddQuestionState,
    callback: AddQuestionCallback
) {
    // Leaving mid-save would hide whether the question was stored.
    BackHandler(enabled = state.isSaving) {}

    Scaffold(
        contentWindowInsets = WindowInsets(),
        containerColor = APP_BACKGROUND,
        modifier = modifier,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 60.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(space = 4.dp)
            ) {
                IconButton(
                    onClick = {
                        callback.handleAction(action = AddQuestionAction.Navigate(destination = AddQuestionDestinations.Back))
                    },
                    enabled = !state.isSaving,
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
                    modifier = Modifier.semantics { heading() },
                    text = stringResource(id = R.string.add_a_question),
                    style = typography.bold16.copy(fontSize = 18.sp),
                    color = colors.espresso
                )
            }
        }
    ) { innerPadding ->
        val quiz = state.quiz ?: return@Scaffold
        val chapter = state.chapter ?: return@Scaffold
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
                AnimatedVisibility(
                    visible = state.lastSaved != null,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    state.lastSaved?.let { saved ->
                        AddQuestionSavedBanner(
                            modifier = Modifier.fillMaxWidth(),
                            saved = saved,
                            chapterNumber = state.chapterNumber
                        )
                    }
                }
                AddQuestionChapterCard(
                    modifier = Modifier.fillMaxWidth(),
                    bookTitle = quiz.title,
                    chapter = chapter
                )
                // A new key after each save gives the next question empty fields.
                key(state.formVersion) {
                    Column(
                        modifier = Modifier.alpha(alpha = if (state.isSaving) 0.6f else 1f),
                        verticalArrangement = Arrangement.spacedBy(space = 20.dp)
                    ) {
                        AddQuestionTextField(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !state.isSaving,
                            callback = callback
                        )
                        AddQuestionChoices(
                            modifier = Modifier.fillMaxWidth(),
                            correctAnswerIndex = state.correctAnswerIndex,
                            duplicateIndices = state.duplicateChoiceIndices,
                            enabled = !state.isSaving,
                            callback = callback
                        )
                    }
                }
            }
            AddQuestionSaveBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                state = state,
                callback = callback
            )
        }
    }
}

private class AddQuestionStateProvider : PreviewParameterProvider<AddQuestionState> {
    private val base = AddQuestionState(quiz = Quiz.dummy(), chapterNumber = 1)
    override val values = sequenceOf(
        base,
        base.copy(
            question = "Who tells Ibarra what happened to his father?",
            choices = listOf("Tenyente Guevara", "Padre Dámaso", "Kapitan Tiyago", "Padre Sibyla"),
            correctAnswerIndex = 0
        ),
        base.copy(
            lastSaved = SavedQuestion(number = 3, question = "Who tells Ibarra what happened to his father?"),
            savedCount = 1
        ),
        base.copy(saveFailed = true)
    )
}

@PreviewLightDark
@Composable
private fun Preview(@PreviewParameter(AddQuestionStateProvider::class) state: AddQuestionState) {
    AppTheme {
        Screen(
            callback = AddQuestionCallback.default(),
            state = state,
            modifier = Modifier.fillMaxSize()
        )
    }
}
