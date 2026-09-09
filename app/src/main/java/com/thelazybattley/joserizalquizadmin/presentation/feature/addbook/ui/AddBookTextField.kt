package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookTextFieldTypes
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonTextField
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class)
@Composable
fun AddBookTextField(
    modifier: Modifier = Modifier,
    type: AddBookTextFieldTypes,
    callback: AddBookCallback
) {
    val state = rememberTextFieldState(initialText = "")
    LaunchedEffect(key1 = Unit) {
        snapshotFlow { state.text }
            .debounce(timeout = 150.milliseconds)
            .collect { searchQuery ->
                callback.handleAction(
                    action = AddBookActions.TextFieldUpdated(
                        text = searchQuery.toString(),
                        type = type
                    )
                )
            }
    }
    Column {
        Text(
            text = stringResource(id = type.id),
            style = typography.semiBold10,
            color = colors.taupe
        )
        CommonTextField(
            state = state,
            modifier = modifier.height(height = 65.dp),
            textAlign = TextAlign.Left,
            textStyle = typography.regular13
        )
    }

}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookTextField(
            modifier = Modifier.fillMaxWidth(),
            type = AddBookTextFieldTypes.TITLE,
            callback = AddBookCallback.default()
        )
    }
}
