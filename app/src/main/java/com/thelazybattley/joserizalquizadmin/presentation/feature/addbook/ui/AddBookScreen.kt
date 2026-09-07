package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookViewModel

@Composable
fun AddBookScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<AddBookViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle()

}
