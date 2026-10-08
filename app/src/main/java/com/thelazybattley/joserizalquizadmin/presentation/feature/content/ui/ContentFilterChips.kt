package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.common.CommonFilterChip
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

@Composable
fun ContentFilterChips(
    modifier: Modifier = Modifier,
    quiz: List<Quiz>,
    selected: Category?,
    callback: ContentCallback
) {
    Row(
        modifier = modifier
            .horizontalScroll(state = rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        CommonFilterChip(
            label = stringResource(id = R.string.all),
            count = quiz.size,
            isSelected = selected == null
        ) {
            callback.handleAction(action = ContentActions.FilterSelected(category = null))
        }
        Category.entries.forEach { category ->
            CommonFilterChip(
                label = stringResource(id = category.id),
                count = quiz.count { it.category == category },
                isSelected = selected == category
            ) {
                callback.handleAction(action = ContentActions.FilterSelected(category = category))
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentFilterChips(
            quiz = listOf(Quiz.dummy(), Quiz.dummy(id = "2").copy(category = Category.NOVEL)),
            selected = null,
            callback = ContentCallback.default()
        )
    }
}
