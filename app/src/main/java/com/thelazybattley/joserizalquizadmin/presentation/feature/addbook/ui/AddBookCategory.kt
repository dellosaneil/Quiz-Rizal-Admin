package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonSegmentedControl
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

@Composable
fun AddBookCategory(
    modifier: Modifier = Modifier,
    selectedCategory: Category,
    callback: AddBookCallback
) {
    CommonSegmentedControl(
        modifier = modifier,
        selectedOption = selectedCategory,
        choices = Category.entries,
        stringResources = Category.entries.map { it.id },
        callback = { category ->
            callback.handleAction(action = AddBookActions.CategoryUpdated(category = category))
        },
        titleRes = R.string.category
    )
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookCategory(
            modifier = Modifier.fillMaxWidth(),
            callback = AddBookCallback.default(),
            selectedCategory = Category.LIFE_OF_RIZAL
        )
    }
}
