package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionAction
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTopBar
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND
import kotlin.time.Duration.Companion.milliseconds

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
    LaunchedEffect(key1 = state.showSuccessBanner) {
        if (state.showSuccessBanner) {
            kotlinx.coroutines.delay(duration = 3000L.milliseconds)
            viewModel.handleAction(action = AddQuestionAction.ResetBanner)
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

    Scaffold(
        contentWindowInsets = WindowInsets(),
        containerColor = APP_BACKGROUND,
        modifier = modifier,
        topBar = {
            CommonTopBar(
                modifier = Modifier.fillMaxWidth(),
                onIconClicked = {
                    callback.handleAction(
                        action = AddQuestionAction.Navigate(
                            destination = AddQuestionDestinations.Back
                        )
                    )
                }
            ) {
                Text(
                    text = stringResource(id = R.string.add_a_question),
                    style = typography.bold16,
                    color = colors.espresso
                )
            }
        }
    ) { innerPadding ->
        state.quiz ?: return@Scaffold
        Box(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(space = 16.dp)
            ) {
                AddQuestionContextCard(
                    modifier = Modifier.fillMaxWidth(),
                    quizName = state.quiz.title,
                    chapterNumber = state.chapterNumber,
                )
                AddQuestionTextField(
                    modifier = Modifier.fillMaxWidth(),
                    callback = callback,
                    showSuccessBanner = state.showSuccessBanner
                )
                AddQuestionChoices(
                    modifier = Modifier.fillMaxWidth(),
                    correctAnswerIndex = state.correctAnswerIndex,
                    callback = callback,
                    showSuccessBanner = state.showSuccessBanner
                )
                Spacer(modifier = Modifier.weight(weight = 1f))
                CommonButton(
                    text = stringResource(id = R.string.save_question),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.isButtonEnabled
                ) {
                    callback.handleAction(action = AddQuestionAction.SaveQuestion)
                }
            }

            AnimatedVisibility(
                visible = state.showSuccessBanner,
                enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                modifier = Modifier.fillMaxWidth()
            ) {
                AddQuestionSuccessBanner(
                    chapterNumber = state.chapterNumber,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        Screen(
            callback = AddQuestionCallback.default(),
            state = AddQuestionState(
                quiz = Quiz.dummy()
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
