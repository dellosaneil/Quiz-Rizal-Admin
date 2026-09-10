package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookTextFieldTypes
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonButton
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTopBar
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun AddBookScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AddBookViewModel>()
    val state by viewModel.state.collectAsStateWithLifecycle()

    AddBookScreen(
        modifier = modifier,
        state = state,
        callback = viewModel
    )
}

@Composable
private fun AddBookScreen(
    modifier: Modifier = Modifier,
    callback: AddBookCallback,
    state: AddBookState
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {
            CommonTopBar(
                modifier = Modifier.fillMaxWidth(),
                onIconClicked = {}
            ) {
                Text(
                    text = stringResource(id = R.string.add_a_book),
                    style = typography.bold14,
                    color = colors.espresso
                )
            }
        },
        containerColor = APP_BACKGROUND,
        contentWindowInsets = WindowInsets()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(paddingValues = innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.weight(weight = 1f),
                verticalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                item {
                    AddBookCategory(
                        modifier = Modifier,
                        selectedCategory = state.category,
                        callback = callback
                    )
                }
                item {
                    AddBookTextFieldTypes.entries.forEach { type ->
                        AddBookTextField(
                            type = type,
                            callback = callback
                        )
                    }
                }
                item {
                    Text(
                        text = stringResource(id = R.string.chapters),
                        style = typography.semiBold10,
                        color = colors.taupe
                    )
                }

                itemsIndexed(items = state.chapters) { index, _ ->
                    AddBookChapterTextField(
                        index = index,
                        modifier = Modifier.fillMaxWidth().height(height = 56.dp),
                        callback = callback
                    )
                }

                item {
                    Text(
                        text = stringResource(id = R.string.add_chapter),
                        style = typography.semiBold11,
                        color = colors.antiqueGold,
                        modifier = Modifier.clickable {
                            callback.handleAction(action = AddBookActions.Chapter.Add)
                        }
                    )
                }
            }
            CommonButton(
                text = stringResource(id = R.string.publish_book),
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isButtonEnabled
            ) {
                callback.handleAction(action = AddBookActions.PublishBook)
            }
        }
    }
}


@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookScreen(
            modifier = Modifier,
            callback = AddBookCallback.default(),
            state = AddBookState()
        )
    }
}
