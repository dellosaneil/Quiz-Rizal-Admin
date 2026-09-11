package com.thelazybattley.joserizalquizadmin.presentation.feature.content.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.thelazybattley.joserizalquizadmin.R
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentActions
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentCallback
import com.thelazybattley.joserizalquizadmin.presentation.feature.content.ContentDestinations
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.colors
import com.thelazybattley.joserizalquizadmin.presentation.ui.theme.AppTheme.typography
import com.thelazybattley.joserizalquizadmin.presentation.util.APP_BACKGROUND

@Composable
fun ContentStickyHeader(modifier: Modifier = Modifier, callback: ContentCallback, count: Int) {
    Row(
        modifier = modifier
            .background(color = APP_BACKGROUND)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = pluralStringResource(id = R.plurals.your_books, count = count),
            style = typography.semiBold11,
            color = colors.taupe
        )
        Text(
            text = stringResource(id = R.string.plus_add_a_book),
            color = colors.antiqueGold,
            style = typography.semiBold11,
            modifier = Modifier.clickable {
                callback.handleAction(action = ContentActions.Navigate(destination = ContentDestinations.AddBook))
            }
        )
    }
}

@PreviewLightDark
@Composable
private fun Preview() {
    AppTheme {
        ContentStickyHeader(
            modifier = Modifier.fillMaxWidth(),
            callback = ContentCallback.default(),
            count = 1
        )
    }
}
