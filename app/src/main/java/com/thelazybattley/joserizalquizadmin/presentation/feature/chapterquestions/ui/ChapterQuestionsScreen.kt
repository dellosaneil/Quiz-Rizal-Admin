package com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Chapter
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Question
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ChapterQuestionsActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ChapterQuestionsCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ChapterQuestionsDestinations
import com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ChapterQuestionsState
import com.thelazybattley.joserizalquizadmin.presentation.feature.chapterquestions.ChapterQuestionsViewModel
import com.thelazybattley.joserizalquizadmin.presentation.feature.moderate.ui.ModerateClearState
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun ChapterQuestionsScreen(
    modifier: Modifier = Modifier,
    navigate: (ChapterQuestionsDestinations) -> Unit
) {
    val viewModel = hiltViewModel<ChapterQuestionsViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = state.destination) {
        state.destination?.let { destination ->
            navigate(destination)
            viewModel.handleAction(action = ChapterQuestionsActions.Navigate(destination = null))
        }
    }
    LifecycleResumeEffect(key1 = Unit) {
        viewModel.handleAction(action = ChapterQuestionsActions.RefreshReports)
        onPauseOrDispose { }
    }
    ChapterQuestionsScreen(modifier = modifier, state = state, callback = viewModel)
}

@Composable
private fun ChapterQuestionsScreen(
    modifier: Modifier = Modifier,
    state: ChapterQuestionsState,
    callback: ChapterQuestionsCallback
) {
    val quiz = state.quiz
    val chapter = state.chapter
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
                    onClick = {
                        callback.handleAction(action = ChapterQuestionsActions.Navigate(destination = ChapterQuestionsDestinations.Back))
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.back),
                        tint = colors.espresso
                    )
                }
                if (quiz != null && chapter != null) {
                    Column {
                        Text(
                            modifier = Modifier.semantics { heading() },
                            text = stringResource(id = R.string.chapter_title, chapter.chapterNumber, chapter.chapterName),
                            style = typography.bold16.copy(fontSize = 18.sp),
                            color = colors.espresso,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = stringResource(
                                id = R.string.dot_separated,
                                quiz.title,
                                pluralStringResource(
                                    id = R.plurals.question_count,
                                    count = chapter.questions.size,
                                    chapter.questions.size
                                )
                            ),
                            style = typography.regular12,
                            color = colors.woodsmokeBrown,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        chapter ?: return@Scaffold
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(weight = 1f),
                contentPadding = PaddingValues(top = 4.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(space = 10.dp)
            ) {
                if (chapter.questions.isEmpty()) {
                    item {
                        ModerateClearState(
                            modifier = Modifier.fillMaxWidth(),
                            titleRes = R.string.no_questions_yet,
                            bodyRes = R.string.no_questions_in_chapter_body
                        )
                    }
                }
                itemsIndexed(items = chapter.questions) { index, question ->
                    ChapterQuestionCard(
                        modifier = Modifier.fillMaxWidth(),
                        number = index + 1,
                        question = question,
                        reportCount = state.reportCounts[question.question] ?: 0
                    ) {
                        callback.handleAction(
                            action = ChapterQuestionsActions.Navigate(
                                destination = ChapterQuestionsDestinations.EditQuestion(question = question.question)
                            )
                        )
                    }
                }
            }
            Button(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .height(height = 52.dp),
                onClick = {
                    callback.handleAction(action = ChapterQuestionsActions.Navigate(destination = ChapterQuestionsDestinations.AddQuestion))
                },
                shape = RoundedCornerShape(size = 12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colors.maroon, contentColor = colors.white)
            ) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(size = 18.dp))
                Text(
                    modifier = Modifier.padding(start = 6.dp),
                    text = stringResource(id = R.string.add_a_question_button),
                    style = typography.bold14
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ChapterQuestionsScreen(
            modifier = Modifier.fillMaxSize(),
            state = ChapterQuestionsState(
                chapterNumber = 1,
                quiz = Quiz.dummy().copy(
                    chapters = listOf(
                        Chapter.dummy(chapterNumber = 1).copy(
                            questions = listOf(Question.dummy(), Question.dummy().copy(question = "Who hosts the dinner?"))
                        )
                    )
                ),
                reportCounts = mapOf(Question.dummy().question to 14)
            ),
            callback = ChapterQuestionsCallback.default()
        )
    }
}
