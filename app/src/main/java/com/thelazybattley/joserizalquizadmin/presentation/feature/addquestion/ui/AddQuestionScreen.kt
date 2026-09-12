package com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addquestion.AddQuestionViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTopBar
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun AddQuestionScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AddQuestionViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()
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
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValues = innerPadding),
            verticalArrangement = Arrangement.spacedBy(space = 16.dp)
        ) {
            AddQuestionContextCard(
                modifier = Modifier.fillMaxWidth(),
                quizName = "Jose Rizal Quiz",
                chapterNumber = 1
            )
            AddQuestionTextField(
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        Screen(
            callback = AddQuestionCallback.default(),
            state = AddQuestionState(),
            modifier = Modifier.fillMaxSize()
        )
    }
}
