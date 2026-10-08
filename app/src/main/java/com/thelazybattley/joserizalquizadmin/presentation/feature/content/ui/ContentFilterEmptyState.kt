package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BORDER_COLOR
import com.thelazybattley.joserizalquizadmin.presentation.util.Category

// Shown when the selected category filter matches no books.
@Composable
fun ContentFilterEmptyState(
    modifier: Modifier = Modifier,
    category: Category,
    onAddBook: () -> Unit,
    onShowAll: () -> Unit
) {
    val shape = RoundedCornerShape(size = 16.dp)
    val categoryName = stringResource(id = category.id)
    Column(
        modifier = modifier
            .background(color = colors.ivoryMist, shape = shape)
            .border(width = 1.dp, color = APP_BORDER_COLOR, shape = shape)
            .padding(horizontal = 22.dp, vertical = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(space = 10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(size = 48.dp)
                .background(color = colors.antiqueCream, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = null,
                tint = colors.antiqueGold
            )
        }
        Text(
            text = stringResource(id = R.string.no_category_books_yet, categoryName),
            style = typography.semiBold17,
            color = colors.espresso,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = category.emptyBodyRes),
            style = typography.regular13.copy(lineHeight = 20.sp),
            color = colors.woodsmokeBrown,
            textAlign = TextAlign.Center
        )
        Column(
            modifier = Modifier
                .padding(top = 6.dp)
                .widthIn(max = 260.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(space = 4.dp)
        ) {
            ContentAddBookButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 44.dp),
                text = stringResource(id = R.string.add_category_book, categoryName),
                onClick = onAddBook
            )
            TextButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 44.dp),
                onClick = onShowAll
            ) {
                Text(
                    text = stringResource(id = R.string.show_all_books),
                    style = typography.bold12.copy(fontSize = 13.sp),
                    color = colors.maroon
                )
            }
        }
    }
}

private val Category.emptyBodyRes: Int
    @StringRes get() = when (this) {
        Category.LIFE_OF_RIZAL -> R.string.no_life_of_rizal_books_body
        Category.NOVEL -> R.string.no_novel_books_body
    }

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentFilterEmptyState(
            modifier = Modifier.fillMaxWidth(),
            category = Category.LIFE_OF_RIZAL,
            onAddBook = {},
            onShowAll = {}
        )
    }
}
