package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
        modifier = modifier,
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
                .padding(all = 16.dp)
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
            }
            CommonButton(
                text = stringResource(id = R.string.publish_book),
                modifier = Modifier.fillMaxWidth()
            ) { }
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
