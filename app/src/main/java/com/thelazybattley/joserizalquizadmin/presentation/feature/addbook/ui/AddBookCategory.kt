package com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.addbook.AddBookCallback
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

@Composable
fun AddBookCategory(
    modifier: Modifier = Modifier,
    selectedCategory: Category,
    enabled: Boolean = true,
    callback: AddBookCallback
) {
    Row(
        modifier = modifier
            .height(intrinsicSize = IntrinsicSize.Min)
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        Category.entries.forEach { category ->
            CategoryCard(
                modifier = Modifier
                    .weight(weight = 1f)
                    .fillMaxHeight(),
                labelRes = category.id,
                hintRes = category.hintRes,
                selected = category == selectedCategory,
                enabled = enabled
            ) {
                callback.handleAction(action = AddBookActions.CategoryUpdated(category = category))
            }
        }
    }
}

private val Category.hintRes: Int
    @StringRes get() = when (this) {
        Category.LIFE_OF_RIZAL -> R.string.life_of_rizal_hint
        Category.NOVEL -> R.string.novel_hint
    }

@Composable
private fun CategoryCard(
    modifier: Modifier = Modifier,
    @StringRes labelRes: Int,
    @StringRes hintRes: Int,
    selected: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(size = 14.dp)
    Column(
        modifier = modifier
            .heightIn(min = 84.dp)
            .clip(shape = shape)
            .background(color = colors.ivoryMist, shape = shape)
            .border(
                width = if (selected) 2.dp else 1.dp,
                color = if (selected) colors.maroon else APP_BORDER_COLOR,
                shape = shape
            )
            .selectable(selected = selected, enabled = enabled, role = Role.RadioButton, onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(space = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(weight = 1f),
                text = stringResource(id = labelRes),
                style = typography.bold14,
                color = colors.espresso
            )
            RadioDot(selected = selected)
        }
        Text(
            text = stringResource(id = hintRes),
            style = typography.regular12,
            color = colors.woodsmokeBrown
        )
    }
}

@Composable
private fun RadioDot(selected: Boolean) {
    if (selected) {
        Box(
            modifier = Modifier
                .size(size = 16.dp)
                .background(color = colors.maroon, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(size = 6.dp)
                    .background(color = colors.ivoryMist, shape = CircleShape)
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(size = 16.dp)
                .border(width = 2.dp, color = colors.taupe, shape = CircleShape)
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        AddBookCategory(
            modifier = Modifier.fillMaxWidth(),
            callback = AddBookCallback.default(),
            selectedCategory = Category.NOVEL
        )
    }
}
