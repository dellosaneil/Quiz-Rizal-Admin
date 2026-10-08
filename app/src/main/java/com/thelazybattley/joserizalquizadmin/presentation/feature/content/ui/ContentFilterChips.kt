package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.domain.model.quiz.Quiz
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR
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
        FilterChip(
            label = stringResource(id = R.string.all),
            count = quiz.size,
            isSelected = selected == null
        ) {
            callback.handleAction(action = ContentActions.FilterSelected(category = null))
        }
        Category.entries.forEach { category ->
            FilterChip(
                label = stringResource(id = category.id),
                count = quiz.count { it.category == category },
                isSelected = selected == category
            ) {
                callback.handleAction(action = ContentActions.FilterSelected(category = category))
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    count: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(size = 18.dp)
    Row(
        modifier = Modifier
            .height(height = 36.dp)
            .clip(shape = shape)
            .background(color = if (isSelected) colors.antiqueCream else colors.ivoryMist, shape = shape)
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) colors.antiqueGold else APP_BORDER_COLOR,
                shape = shape
            )
            .selectable(selected = isSelected, role = Role.RadioButton, onClick = onClick)
            .padding(start = 14.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Text(
            text = label,
            style = typography.semiBold13,
            color = colors.espresso
        )
        Text(
            text = count.toString(),
            style = typography.regular11.copy(fontFamily = FontFamily.Monospace),
            color = if (isSelected) colors.antiqueGold else colors.woodsmokeBrown
        )
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
