package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookState
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookViewModel
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTopBar
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
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
            ) { }
        },
        containerColor = APP_BACKGROUND,
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier,
        ) {

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
